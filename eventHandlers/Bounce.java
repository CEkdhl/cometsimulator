package cometsim.eventHandlers;

/*
 * Example of an event handler which will Stop the integration
 * if any object gets too close to the sun at (0, 0, 0)
 */

import org.apache.commons.math3.ode.events.EventHandler;

public class Bounce implements EventHandler {

    double distanceThreshold;
    int sign = 1;

    public Bounce(double distanceThreshold) {
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
            double norm = Math.sqrt(y[6*i]*y[6*i]+y[6*i+1]*y[6*i+1]+y[6*i+2]*y[6*i+2]);
            if(norm-distanceThreshold < minDistance) minDistance = norm-distanceThreshold;
        }

        return sign*minDistance;
    }

    public EventHandler.Action eventOccurred(double t, double[] y, boolean increasing) {
        sign = -1*sign;

        return Action.RESET_STATE;
    }

    public void resetState(double t, double[] y) {
        double minDistance = 1E10; // Might have to change this. I don't know what works.
        int index = 0;

        for(int i=0; i < y.length/6; i++) {
            double norm = Math.sqrt(y[6*i]*y[6*i]+y[6*i+1]*y[6*i+1]+y[6*i+2]*y[6*i+2]);
            if(norm-distanceThreshold < minDistance) {
                minDistance = norm-distanceThreshold;
                index = i;
            }
        }

        y[index*6+3] = -y[index*6+3];
        y[index*6+4] = -y[index*6+4];
        y[index*6+5] = -y[index*6+5];
    }

}
