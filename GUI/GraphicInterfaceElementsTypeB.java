package cometsim.GUI;

import com.sun.j3d.utils.applet.MainFrame;
import cometsim.*;
import cometsim.XYZCoordinates;
import cometsim.ElementsTypeB;
import cometsim.planets.*;
import cometsim.saveAndLoad.LoadResults;

import static cometsim.Constants.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class GraphicInterfaceElementsTypeB {
    
    public static void main(String[] args) {
        JTextField semimajorAxisField = new JTextField(5);
        JTextField eccentricityField = new JTextField(5);
        JTextField inclinationField = new JTextField(5);
        JTextField meanLongitudeField = new JTextField(5);
        JTextField perihelionLongitudeField = new JTextField(5);
        JTextField ascendingNodeLongitudeField = new JTextField(5);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2,3));
        inputPanel.add(new JLabel("Semimajor axis (a) (AU):"));
        inputPanel.add(semimajorAxisField);
        inputPanel.add(new JLabel("Eccentricity (e) (rad):"));
        inputPanel.add(eccentricityField);
        inputPanel.add(new JLabel("Inclination (i) (degree):"));
        inputPanel.add(inclinationField);
        inputPanel.add(new JLabel("Mean longitude (L) (degree):"));
        inputPanel.add(meanLongitudeField);
        inputPanel.add(new JLabel("Longitude of perihelion (omega) (degree):"));
        inputPanel.add(perihelionLongitudeField);
        inputPanel.add(new JLabel("Longitude of ascending node (OMEGA) (degree):"));
        inputPanel.add(ascendingNodeLongitudeField);
        
        int input = JOptionPane.showConfirmDialog(null, inputPanel,
                "Startvalues", JOptionPane.DEFAULT_OPTION);
        
        if (input == JOptionPane.OK_OPTION) {
            double semimajorAxis = Double.parseDouble(semimajorAxisField.getText());
            double eccentricity = Double.parseDouble(eccentricityField.getText());
            double inclination = Double.parseDouble(inclinationField.getText());
            double meanLongitude = Double.parseDouble(meanLongitudeField.getText());
            double perihelionLongitude = Double.parseDouble(perihelionLongitudeField.getText());
            double ascendingNodeLongitude = Double.parseDouble(ascendingNodeLongitudeField.getText());
            
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
            sim.add(new UnknownBody("Comet", cometMass, new ElementsTypeB(semimajorAxis,
                    eccentricity , inclination, meanLongitude, perihelionLongitude, ascendingNodeLongitude)));
            
            sim.simulate(0,1000);
            
            ArrayList<Result> res = new LoadResults(sim).getResults();
            
            new MainFrame(new Visualization(res, (int) sim.getT0(),
                    (int) sim.getTf()), 800, 800);
            
        }
    }
    
}
