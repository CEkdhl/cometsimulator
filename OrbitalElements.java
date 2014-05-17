package cometsim;

import java.io.Serializable;

/**
 * All classes which encapsulate orbital elements extend this abstract class. This class guarantees that its children implement the coordinates methods in a consistent manner.
 */
public abstract class OrbitalElements implements Serializable {

    /**
     * Calculates the xyz position relative to the reference plane on a given date based on the orbital elements given.
     * @param julian Julian day
     * @return xyz coordinates (double[])
     */
    abstract double[] coordinates(double julian);

    /**
     * Calculates the initial state vector based on the orbital elements given.
     * @return State vector {x,y,z,dx,dy,dz} (double[])
     */
    abstract double[] getIVs();

}