package cometsim.eventHandlers;

/*
 * Example of an event handler which will Stop the integration
 * if any object gets too close to the sun at (0, 0, 0)
 */

import cometsim.KnownBody;
import org.apache.commons.math3.ode.events.EventHandler;

import java.util.ArrayList;

public class Stop implements EventHandler {

    double distanceThreshold;
    ArrayList<KnownBody> knownBodies;

    public Stop(double distanceThreshold) {
        this.distanceThreshold = distanceThreshold;
    }

    public void init(double t0, double[] y0, double t) {

    }

    /*
     * This function is evaluated at every step.
     * If its return value switches sign then that triggers eventOccurred.
     * This function needs to be continuous.
     */
    public double g(double t, double[] y) {
        double minDistance = 1E10; // Might have to change this. I don't know what works.

        for(int i=0; i < y.length/6; i++) {
            for(int j=0; j<knownBodies.size(); j++){
                double[] c = knownBodies.get(j).coordinates(t);
                double deltax = y[6*i]-c[0];
                double deltay = y[6*i+1]-c[1];
                double deltaz = y[6*i+2]-c[2];
                double norm = Math.sqrt(deltax*deltax+deltay*deltay+deltaz*deltaz);
                if(norm-distanceThreshold < minDistance) minDistance = norm-distanceThreshold;
            }
        }

        return minDistance;
    }

    public EventHandler.Action eventOccurred(double t, double[] y, boolean increasing) {
        return EventHandler.Action.STOP;
    }

    public void resetState(double t, double[] y) {

    }

    public void setKnownBodies(ArrayList<KnownBody> knownBodies) {
        this.knownBodies = knownBodies;
    }

}
