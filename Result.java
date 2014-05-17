package cometsim;

import org.apache.commons.math3.ode.ContinuousOutputModel;

import java.util.ArrayList;

/**
 * This class encapsulates the result from a simulation and provides helpful utility functions that are needed to process the data.
 * Basically the purpose of this class is to move logic and calculations out of the visualization class. Each instance represents one body.
 */
public class Result {

    private Simulator sim;
    int offset;

    /**
     * Basic constructor.
     * @param sim Simulator object with results (Simulator)
     * @param offset The index of the unknown body in the list of unknown bodies (int)
     */
    public Result(Simulator sim, int offset) {
        this.sim = sim;
        this.offset = offset;
    }

    /**
     * Get the list of known bodies added to the simulator.
     * @return list of known bodies (ArrayList<KnownBody>)
     */
    public ArrayList<KnownBody> getKnownBodies() {
        return sim.getKnownBodies();
    }

    /**
     * Get the list of unknown bodies added to the simulator.
     * @return list of unknown bodies (ArrayList<UnknownBody>)
     */
    public ArrayList<UnknownBody> getUnknownBodies() {
        return sim.getUnknownBodies();
    }

    /**
     * Get the ID of the body.
     * @return ID (String)
     */
    public String getID() {
        return sim.getUnknownBodies().get(offset).getID();
    }

    /**
     * Get the epoch of the simulation
     * @return epoch (double)
     */
    public double getT0() {
        return sim.getT0();
    }

    /**
     * Get the time at which the simulation ends
     * @return ending time (double)
     */
    public double getTf() {
        return sim.getTf();
    }

    /**
     * Gets the coordinates at Julian day t.
     * @param t Julian day (double)
     * @return xyz coordinates (double[3])
     */
    public double[] coordinates(double t) {
        ContinuousOutputModel m = sim.getStepHandler();
        m.setInterpolatedTime(t);
        double[] arr = m.getInterpolatedState();
        double[] c = new double[3];
        c[0] = arr[6*offset];
        c[1] = arr[6*offset+1];
        c[2] = arr[6*offset+2];

        return c;
    }

    /**
     * Get the distance to the sun at Julian day t.
     * @param t Julian day (double)
     * @return distance (double, AUs)
     */
    public double distanceToSun(double t) {
        double[] c = coordinates(t);
        return Math.sqrt(c[0]*c[0]+c[1]*c[1]+c[2]*c[2]);
    }

    /**
     * Return the time t where the body is at its perihelion.
     * @return Julian day (double)
     */
    public double perihelion() {
        double dist;
        double min = distanceToSun(sim.getT0());

        // Figure out a first guess
        for(int t=(int)Math.ceil(sim.getT0()); t<Math.floor(sim.getTf()); t++ ) {
            dist = distanceToSun(t);
            if(dist < min) min = dist;
        }

        // Now preferably use Apache Commons Math optimization package to find a more exact value of t

        return min;
    }

    /**
     * Return the time t where the body is at its aphelion.
     * @return Julian day (double)
     */
    public double aphelion() {
        double dist;
        double max = distanceToSun(sim.getT0());

        // Figure out a first guess
        for(int t=(int)Math.ceil(sim.getT0()); t<Math.floor(sim.getTf()); t++ ) {
            dist = distanceToSun(t);
            if(dist > max ) max = dist;
        }

        // Now preferably use Apache Commons Math optimization package to find a more exact value of t

        return max;
    }
}
