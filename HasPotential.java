package cometsim;

import cometsim.UnknownBody;

public interface HasPotential {

    public double getPotential(double t, double[] y, UnknownBody body);

}
