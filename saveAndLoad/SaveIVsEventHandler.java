package cometsim.saveAndLoad;

import cometsim.UnknownBody;
import org.apache.commons.math3.ode.events.EventHandler;

import java.util.ArrayList;

import static cometsim.Constants.G;
import static cometsim.Constants.sunMass;
import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

public class SaveIVsEventHandler implements EventHandler {

    ArrayList<UnknownBody> bodies = new ArrayList<UnknownBody>();

    public SaveIVsEventHandler(ArrayList<UnknownBody> bodies) {
        this.bodies = bodies;
    }

    public void init(double t0, double[] y0, double t) {

    }

    public double g(double t, double[] y) {
        return t-1.5*2*PI/sqrt(G*sunMass);
    }

    public Action eventOccurred(double t, double[] y, boolean increasing) {
        HandleIVs IVsaver = new HandleIVs();
        IVsaver.saveIVs("savedIVs",new IVs(bodies,y).getList());

        return Action.STOP;
    }

    public void resetState(double t, double[] y) {

    }
}
