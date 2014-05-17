
package cometsim;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.Sphere;
import static cometsim.Constants.*;
import javax.media.j3d.GeometryArray;
import cometsim.planets.*;
import cometsim.saveAndLoad.LoadResults;

import java.util.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Visualization extends Applet implements ActionListener, KeyListener {
    
    private final int forwardSteps=100;
    private final Button goStop = new Button(" Go ");
    private final Button speedButton = new Button("Change Speed");
    private final Button scaleButton = new Button("Change Scale");
    private final Button reset = new Button("Reset");
    private TransformGroup objTrans[];
    Result [] bodies;
    private int t;
    private Transform3D [] pos;
    private Timer timer;
    int speed = 10;
    double scale;
    private final int nbrKnownObjects,nbrUnknownObjects,
            timeInterval,nbrObjects,t0,tmax;
    double size = 0.1;
    private double startposX, startposY, startposZ;
    private double[] coordinates=new double[3];
    private boolean running = false;
    TransformGroup viewTransform;
    Transform3D view3d;
    Viewer viewer;
    View view;
    ViewingPlatform viewPlatform;
    SimpleUniverse universe;
    Point3d[] pts;
    Shape3D lineShape3D [];
    LineArray line;
    
    /**
     * Constructor which creates the universe that we can see things in.
     *
     * @param bodiesArrayList Array of Results.
     * @param t0 Starting time.
     * @param tmax Ending time.
     */
    public Visualization(ArrayList<Result> bodiesArrayList, int t0, int tmax)
    {
        Result[] bodies = new Result[bodiesArrayList.size()];
        bodies = bodiesArrayList.toArray(bodies);
        
        this.bodies = bodies;
        this.nbrKnownObjects = bodies[0].getKnownBodies().size();
        this.nbrUnknownObjects = bodies.length;
        this.nbrObjects = nbrUnknownObjects+nbrKnownObjects;
        this.t = t0;
        this.t0 = t0;
        this.tmax = tmax;
        this.scale = bodies[0].aphelion();
        for(int i=0; i<nbrUnknownObjects; i++)
        {
            if(bodies[i].aphelion() > scale )
            {
                this.scale=bodies[i].aphelion();
            }
        }
        this.timeInterval = tmax-t0;
        
        // This is som JAVA 3D stuff which we must have to create a visualization
        // I don't understand it completely
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D c = new Canvas3D(config);
        add("Center", c);
        c.addKeyListener(this);
        
        viewPlatform = new ViewingPlatform();
        viewPlatform.getViewPlatform().setActivationRadius((float) scale);
        
        viewTransform = viewPlatform.getViewPlatformTransform();
        view3d = new Transform3D();
        
        view3d.lookAt(new Point3d(0, 0, scale * 3), new Point3d(0, 0, 0),
                new Vector3d(0, 1, 0));
        view3d.invert();
        viewTransform.setTransform(view3d);
        
        Viewer viewer = new Viewer(c);
        View view = viewer.getView();
        view.setBackClipDistance(100000000);
        
        // Create a simple scene and attach it to the virtual universe
        BranchGroup scene = createSceneGraph(bodies);
        universe = new SimpleUniverse(viewPlatform, viewer);
        universe.addBranchGraph(scene);
        
        // adds the buttons to a panel
        Panel p = new Panel();
        p.add(goStop);
        
        p.add(speedButton);
        p.add(scaleButton);
        p.add(reset);
        add("North", p);
        
        // add listeners to all buttons so things happen when we push them
        goStop.addActionListener(this);
        goStop.addKeyListener(this);
        
        speedButton.addActionListener(this);
        speedButton.addKeyListener(this);
        
        scaleButton.addActionListener(this);
        scaleButton.addKeyListener(this);
        
        reset.addActionListener(this);
        reset.addKeyListener(this);
        
        timer = new Timer(speed,this);
        
    }
    
    /**
     * Paints all the bodies in the beginning.
     * @param bodies Array with Result.
     * @return
     */
    public BranchGroup createSceneGraph(Result[] bodies)
    {
        
        // Branchgroup will have all things we want to paint
        // Again JAVA 3D thing don't really understand how it works
        BranchGroup objRoot = new BranchGroup();
        
        //This is a group which will contain all objects we will want to paint
        objTrans = new TransformGroup[forwardSteps*nbrObjects+nbrObjects];
        lineShape3D = new Shape3D[nbrObjects];
        // Transform3D is the object we will have to draw (planet, comet etc.)
        pos = new Transform3D[forwardSteps*nbrObjects+nbrObjects];
        Sphere sphere;
        for(int i=0; i<nbrKnownObjects; i++)
        {
            Color3f color = bodies[0].getKnownBodies().get(i).getColor();
            
            //Creates the material (color on the objects)
            Material sphereMaterial = new Material();
            if(i == 0)
            {
                sphereMaterial.setEmissiveColor(color);
                sphereMaterial.setSpecularColor(color);
            }else
            {
                sphereMaterial.setDiffuseColor(color);
                sphereMaterial.setAmbientColor(color);
            }
            Appearance sphereAppearance = new Appearance();
            sphereAppearance.setMaterial(sphereMaterial);
            
            //creates the spehere
            sphere = new Sphere((float) size, sphereAppearance);
            //This creates the transformgroup and allow us to write
            objTrans[i] = new TransformGroup();
            objTrans[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            
            // This will draw the objects
            pos[i] = new Transform3D();
            
            coordinates = bodies[0].getKnownBodies().get(i).coordinates(t);
            startposX = coordinates[0];
            startposY = coordinates[1];
            startposZ = coordinates[2];
            
            pos[i].setTranslation(new Vector3f((float)(startposX),
                    (float)(startposY),(float)(startposZ)));
            objTrans[i].setTransform(pos[i]);
            objTrans[i].addChild(sphere);
            objRoot.addChild(objTrans[i]);
            
            
            // in this for loop we will draw the line which now is drawing the
            // future of the object. (can be redone so it will draw a tail for
            // the objects.)
            /*
            for(int j=0;j<forwardSteps;j++)
            {
            Material sphereMaterial1 = new Material();
            sphereMaterial1.setEmissiveColor(1.0f,1.0f,1.0f);
            Appearance sphereAppearance1 = new Appearance();
            sphereAppearance1.setMaterial(sphereMaterial1);
            sphere = new Sphere((float) (size/10), sphereAppearance1);
            objTrans[nbrObjects+i*forwardSteps+j] = new TransformGroup();
            objTrans[nbrObjects+i*forwardSteps+j].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            pos[nbrObjects+i*forwardSteps+j] = new Transform3D();
            
            coordinates = bodies[0].getKnownBodies().get(i).coordinates(t+j);
            startposX = coordinates[0];
            startposY = coordinates[1];
            startposZ = coordinates[2];
            
            pos[nbrObjects+i*forwardSteps+j].setTranslation( new Vector3f(
            (float)(startposX),(float)(startposY),(float)(startposZ)));
            objTrans[nbrObjects+i*forwardSteps+j].setTransform(pos[nbrObjects+i*forwardSteps+j]);
            objTrans[nbrObjects+i*forwardSteps+j].addChild(sphere);
            objRoot.addChild(objTrans[nbrObjects+i*forwardSteps+j]);
            
            
            }
            */
            Point3d[] pts = new Point3d[forwardSteps];
            for(int j=0; j<forwardSteps; j++) {
                coordinates = bodies[0].getKnownBodies().get(i).coordinates(t+j);
                pts[j] = new Point3d(coordinates[0],coordinates[1],coordinates[2]);
            }
            line = new LineArray(pts.length, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
            for(int k=0;k<forwardSteps;k++){
                line.setColor(k,new Color3f(0f,0f,0f));
            }
            line.setCoordinates(0,pts);
            lineShape3D[i] = new Shape3D(line);
            lineShape3D[i].setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
            objRoot.addChild(lineShape3D[i]);
            
        }
        
        for(int i=nbrKnownObjects; i<nbrObjects; i++)
        {
            
            
            //Creates the material (color on the objects)
            Material sphereMaterial = new Material();
            //whiteSphMaterial.setEmissiveColor(1.0f,1.0f,1.5f);
            Appearance sphereAppearance = new Appearance();
            sphereAppearance.setMaterial(sphereMaterial);
            
            //creates the spehere
            sphere = new Sphere((float) size, sphereAppearance);
            //This creates the transformgroup and allow us to write
            objTrans[i] = new TransformGroup();
            objTrans[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            
            // This will draw the objects
            pos[i] = new Transform3D();
            coordinates = bodies[i-nbrKnownObjects].coordinates(t);
            startposX = coordinates[0];
            startposY = coordinates[1];
            startposZ = coordinates[2];
            pos[i].setTranslation(new Vector3f((float)(startposX),
                    (float)(startposY),(float)(startposZ)));
            objTrans[i].setTransform(pos[i]);
            objTrans[i].addChild(sphere);
            objRoot.addChild(objTrans[i]);
            
            
            // in this for loop we will draw the line which now is drawing the
            // future of the object. (can be redone so it will draw a tail for
            // the objects.)
            /*
            for(int j=0;j<forwardSteps;j++)
            {
            Material sphereMaterial1 = new Material();
            sphereMaterial1.setEmissiveColor(1.0f,1.0f,1.5f);
            Appearance sphereAppearance1 = new Appearance();
            sphereAppearance1.setMaterial(sphereMaterial1);
            sphere = new Sphere((float) (size/10), sphereAppearance1);
            objTrans[nbrObjects+i*forwardSteps+j]= new TransformGroup();
            objTrans[nbrObjects+i*forwardSteps+j].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            pos[nbrObjects+i*forwardSteps+j] = new Transform3D();
            coordinates = bodies[i-nbrKnownObjects].coordinates(t+j);
            startposX = coordinates[0];
            startposY = coordinates[1];
            startposZ = coordinates[2];
            pos[nbrObjects+i*forwardSteps+j].setTranslation(new Vector3f(
            (float)(startposX),(float)(startposY),(float)(startposZ)));
            objTrans[nbrObjects+i*forwardSteps+j].setTransform(pos[nbrObjects+i*forwardSteps+j]);
            objTrans[nbrObjects+i*forwardSteps+j].addChild(sphere);
            objRoot.addChild(objTrans[nbrObjects+i*forwardSteps+j]);
            
            }
            */
            
            Point3d[] pts = new Point3d[forwardSteps];
            for(int j=0; j<forwardSteps; j++) {
                coordinates = bodies[i-nbrKnownObjects].coordinates(t+j);
                pts[j] = new Point3d(coordinates[0],coordinates[1],coordinates[2]);
            }
            
            for(int j=0; j<forwardSteps; j++) {
                coordinates = bodies[i-nbrKnownObjects].coordinates(t+j);
                pts[j] = new Point3d(coordinates[0],coordinates[1],coordinates[2]);
            }
            line = new LineArray(pts.length, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
            for(int k=0;k<forwardSteps;k++){
                line.setColor(k,new Color3f(0f,0f,0f));
            }
            line.setCoordinates(0,pts);
            lineShape3D[i] = new Shape3D(line);
            lineShape3D[i].setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
            objRoot.addChild(lineShape3D[i]);
        }
        
        
        // Here are som things that makes the light.
        
        //Background color
        Background background = new Background(new Color3f(1f,1f,1f));
        
        BoundingSphere bounds =
                new BoundingSphere(new Point3d(0.0,0.0,0.0), 10000000.0);
        
        background.setApplicationBounds(bounds);
        viewPlatform.addChild(background);
        /*
        Color3f light1Color = new Color3f(0.5f, 0.50f, 0.50f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -10.0f);
        DirectionalLight light1
        = new DirectionalLight(light1Color, light1Direction);
        
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);
        */
        PointLight light = new PointLight();
        light.setCapability(PointLight.ALLOW_POSITION_WRITE);
        light.setColor(new Color3f(Color.WHITE));
        light.setPosition(0.0f,0.0f,0.0f);
        light.setInfluencingBounds(bounds);
        objRoot.addChild(light);
        
        /*
        Color3f ambientColor = new Color3f(1.0f, 1.0f, 1.0f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);*/
        return objRoot;
        
    }
    
    
    /**
     * Method that repaints for every timestep
     */
    public void repaint()
    {
        
        t+=1;
        // The things in here will move the object
        if(t<tmax)
        {
            
            for(int i=0; i<nbrKnownObjects; i++)
            {
                coordinates = bodies[0].getKnownBodies().get(i).coordinates(t);
                startposX = coordinates[0];
                startposY = coordinates[1];
                startposZ = coordinates[2];
                //System.out.println("x= "+startposX+ " y= "+startposY+ " z= " +startposZ);
                pos[i].setTranslation(new Vector3f((float)(startposX),
                        (float)(startposY),(float)(startposZ)));
                objTrans[i].setTransform(pos[i]);
                
                
                // in this for loop we will draw the line which now is drawing the
                // future of the object. (can be redone so it will draw a tail for
                // the objects.)
                /*
                for(int j=0;j<forwardSteps;j++){
                
                coordinates = bodies[0].getKnownBodies().get(i).coordinates(t+j);
                startposX = coordinates[0];
                startposY = coordinates[1];
                startposZ = coordinates[2];
                
                pos[nbrObjects+i*forwardSteps+j].setTranslation(new Vector3f(
                (float)(startposX),(float)(startposY),(float)(startposZ)));
                objTrans[nbrObjects+i*forwardSteps+j].setTransform(pos[nbrObjects+i*forwardSteps+j]);
                
                
                }
                */
                
                Point3d[] pts = new Point3d[forwardSteps];
                for(int j=0; j<forwardSteps; j++) {
                    coordinates = bodies[0].getKnownBodies().get(i).coordinates(t+j);
                    pts[j] = new Point3d(coordinates[0],coordinates[1],coordinates[2]);
                }
                line = new LineArray(pts.length, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
                for(int k=0;k<forwardSteps;k++){
                    line.setColor(k,new Color3f(0f,0f,0f));
                }
                line.setCoordinates(0,pts);
                lineShape3D[i].setGeometry(line);
                
                
            }
            
            for(int i=nbrKnownObjects; i<nbrObjects; i++)
            {
                
                coordinates = bodies[i-nbrKnownObjects].coordinates(t);
                startposX = coordinates[0];
                startposY = coordinates[1];
                startposZ = coordinates[2];
                pos[i].setTranslation(new Vector3f(
                        (float) (startposX),(float)(startposY),(float) (startposZ)));
                objTrans[i].setTransform(pos[i]);
                /*
                for(int j=0;j<forwardSteps;j++)
                {
                if(t+j<tmax)
                {
                coordinates=bodies[i-nbrKnownObjects].coordinates(t+j);
                startposX = coordinates[0];
                startposY = coordinates[1];
                startposZ = coordinates[2];
                //        trans[nbrObjects*(i+1)+j]=new Transform3D();
                pos[nbrObjects+i*forwardSteps+j].setTranslation(new Vector3f(
                (float) (startposX),(float)(startposY),(float) (startposZ)));
                objTrans[nbrObjects+i*forwardSteps+j].setTransform(pos[nbrObjects+i*forwardSteps+j]);
                
                }
                }*/
                
                Point3d[] pts = new Point3d[forwardSteps];
                for(int j=0; j<forwardSteps; j++) {
                    if(t+j<tmax){
                    coordinates = bodies[i-nbrKnownObjects].coordinates(t+j);
                    pts[j] = new Point3d(coordinates[0],coordinates[1],coordinates[2]);
                    }else{
                        coordinates = bodies[i-nbrKnownObjects].coordinates(tmax);
                        pts[j] = new Point3d(coordinates[0],coordinates[1],coordinates[2]);
                    }
                   
                }
                line = new LineArray(pts.length, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
                for(int k=0;k<forwardSteps;k++){
                    line.setColor(k,new Color3f(0f,0f,0f));
                }
                line.setCoordinates(0,pts);
                lineShape3D[i].setGeometry(line);
                        
                
            }
        }
    }
    
    
    // No use right now
    public void keyPressed(KeyEvent e)
    {
        
    }
    
    public void keyReleased(KeyEvent e)
    {
        // Invoked when a key has been released.
    }
    
    public void keyTyped(KeyEvent e)
    {
        //Invoked when a key has been typed.
    }
    
    
    
    public void actionPerformed(ActionEvent e )
    {
        
        if (e.getSource() == goStop)
        {
            if(this.running)
            {
                // stops the timer
                this.running = false;
                timer.stop();
                goStop.setLabel("Go");
            }
            else
            {
                // starts the timer
                this.running = true;
                timer.start();
                goStop.setLabel("Stop");
            }
            
            
        }else if(e.getSource() == scaleButton)
        {
            this.running = false;
            timer.stop();
            goStop.setLabel("Go");
            JTextField scaleField = new JTextField(10);
            JPanel inputPanel = new JPanel();
            inputPanel.add(new JLabel("New Scale:"));
            inputPanel.add(scaleField);
            int scaleInput = JOptionPane.showConfirmDialog(null, inputPanel,
                    "Change Scale", JOptionPane.DEFAULT_OPTION);
            if (scaleInput == JOptionPane.OK_OPTION) {
                this.scale=Double.parseDouble(scaleField.getText());
                view3d.lookAt(new Point3d(0,0,scale),
                        new Point3d(0,0,0), new Vector3d(0,1,0));
                view3d.invert();
                viewTransform.setTransform(view3d);
                
            }
            
            // This will change where you are looking from
            
            
        }else if(e.getSource() == speedButton)
        {
            this.running = false;
            timer.stop();
            goStop.setLabel("Go");
            JTextField speedField = new JTextField(5);
            JPanel inputPanel = new JPanel();
            inputPanel.add(new JLabel("New Speed:"));
            inputPanel.add(speedField);
            int speedInput = JOptionPane.showConfirmDialog(null, inputPanel,
                    "Change Speed", JOptionPane.DEFAULT_OPTION);
            if (speedInput == JOptionPane.OK_OPTION) {
                this.speed=Integer.parseInt(speedField.getText());
                
            }
            timer = new Timer(speed,this);
            // changes the speed
            // the higher the value the slower it will draw
            
            
        }else if(e.getSource() == reset)
        {
            this.running = false;
            timer.stop();
            goStop.setLabel("Go");
            t = t0;
            repaint();
            // paint it as it were from the start
            
        }else
        {
            repaint();
            // will paint how it looks after next step
        }
    }
    
    
    
    public static void main(String[] args)
    {
        
        System.out.println("Simulation started");
        Simulator sim = new Simulator();
        
        sim.add(new KnownBody("Sun", sunMass, new XYZCoordinates(0,0,0,0,0,0)));
        sim.add(new Mercury());
        sim.add(new Venus());
        sim.add(new Earth());
        sim.add(new Mars());
        sim.add(new Jupiter());
        sim.add(new Saturn());
        sim.add(new Uranus());
        sim.add(new Neptune());
        
        sim.add(new UnknownBody("Comet", cometMass,
                new XYZCoordinates(3,0,0,0,0.015/2,0)));
        sim.simulate(0,1000);
        
        ArrayList<Result> res = new LoadResults(sim).getResults();
        
        new MainFrame(new Visualization(res, (int) sim.getT0(), (int) sim.getTf()), 800, 800);
        
        
        
    }
    
    
    
}

