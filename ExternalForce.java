package cometsim;

public abstract class ExternalForce {

    public abstract double[] getAcceleration(double t, double y[], UnknownBody body);

}
