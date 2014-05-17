package cometsim;

import cometsim.ExternalForce;
import cometsim.UnknownBody;
import org.apache.commons.math3.util.FastMath;
import static cometsim.Constants.sunMass;
import static cometsim.Constants.G;

public class GalacticTide extends ExternalForce {

    private double G1 = -5.30726E-21;
    private double G2 = 5.30726E-21;
    private double G3 = 4.2332E-20;
    private double rho0 = sunMass/1.13961E-17;
    private double Omega0 = 7.28015E-11;

    public double[] getAcceleration(double t, double[] y, UnknownBody body) {

        double mu = G*sunMass;
        double r = FastMath.sqrt(y[0]*y[0]+y[1]*y[1]+y[2]*y[2]);

        double xprime = y[0]*FastMath.cos(Omega0*t)+y[1]*FastMath.sin(Omega0*t);
        double yprime = -y[0]*FastMath.sin(Omega0*t)+y[1]*FastMath.cos(Omega0*t);

        double[] a = new double[3];
        a[0] = -mu*y[0]/(r*r*r)-G1*xprime*FastMath.cos(Omega0*t)+G2*yprime*FastMath.sin(Omega0*t);
        a[1] = -mu*y[1]/(r*r*r)-G1*xprime*FastMath.sin(Omega0*t)+G2*yprime*FastMath.cos(Omega0*t);
        a[2] = -mu*y[2]/(r*r*r)-G3*y[2];

        return a;
    }

}
