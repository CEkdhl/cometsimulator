package cometsim.GUI;

import com.sun.j3d.utils.applet.MainFrame;
import cometsim.*;
import cometsim.XYZCoordinates;
import cometsim.planets.*;
import cometsim.saveAndLoad.LoadResults;

import static cometsim.Constants.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class GraphicInterfaceXYZ {
    
    public static void main(String[] args) {
        
        JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);
        JTextField zField = new JTextField(5);
        JTextField xvField = new JTextField(5);
        JTextField yvField = new JTextField(5);
        JTextField zvField = new JTextField(5);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2,3));
        inputPanel.add(new JLabel("x:"));
        inputPanel.add(xField);
        inputPanel.add(new JLabel("y:"));
        inputPanel.add(yField);
        inputPanel.add(new JLabel("z:"));
        inputPanel.add(zField);
        inputPanel.add(new JLabel("xv:"));
        inputPanel.add(xvField);
        inputPanel.add(new JLabel("yv:"));
        inputPanel.add(yvField);
        inputPanel.add(new JLabel("zv:"));
        inputPanel.add(zvField);
        
        int input = JOptionPane.showConfirmDialog(null, inputPanel,
                "Startvalues", JOptionPane.DEFAULT_OPTION);
        
        if (input == JOptionPane.OK_OPTION) {
            double x=Double.parseDouble(xField.getText());
            double y=Double.parseDouble(yField.getText());
            double z=Double.parseDouble(zField.getText());
            double xv=Double.parseDouble(xvField.getText());
            double yv=Double.parseDouble(yvField.getText());
            double zv=Double.parseDouble(zvField.getText());
            
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
            
            sim.add(new UnknownBody("Comet", cometMass, new XYZCoordinates(x, y, z, xv, yv, zv)));
            
            sim.simulate(0,1000);
            
            ArrayList<Result> res = new LoadResults(sim).getResults();
            
            new MainFrame(new Visualization(res, (int) sim.getT0(),
                    (int) sim.getTf()), 800, 800);
            
        }
        
        
    }
    
}
