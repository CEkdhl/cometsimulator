package cometsim.saveAndLoad;

import com.sun.j3d.utils.applet.MainFrame;
import cometsim.*;
import cometsim.XYZCoordinates;
import cometsim.ElementsTypeB;

import java.util.ArrayList;

import static cometsim.Constants.G;
import static cometsim.Constants.cometMass;
import static cometsim.Constants.sunMass;
import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

public class SaveSerializedExample {

    public static void main(String[] args) {

        Simulator sim = new Simulator();

        sim.add(new KnownBody("Sun", sunMass, new XYZCoordinates(0,0,0,0,0,0) ));

        sim.add(new UnknownBody("Comet", cometMass, new ElementsTypeB(2, 0, 0, 0, 0, 0)));

        sim.simulate(0, 10*2*PI/sqrt(G*sunMass));

        SaveResults saver = new SaveResults();
        saver.saveSerialized("CircularOrbit",sim);

        LoadResults loadedSim = new LoadResults("CircularOrbit");
        ArrayList<Result> res = loadedSim.getResults();

        new MainFrame(new Visualization(res, (int) loadedSim.getT0(), (int) loadedSim.getTf()), 800, 800);
    }

}
