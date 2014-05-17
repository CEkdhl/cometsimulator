package cometsim;

import static cometsim.Constants.G;
import static cometsim.Constants.sunMass;
import static java.lang.Math.*;

/**
 * ElementsTypeB is like ElementsTypeA the velocity is determined so that the body
 * stays on the orbit defined by the orbital elements.
 */
public class ElementsTypeB extends ElementsTypeA {

    /**
     * Basic constructor.
     * @param semimajor_axis
     * @param eccentricity
     * @param inclination
     * @param mean_longitude
     * @param perihelion_longitude
     * @param ascending_node_longitude
     */
    public ElementsTypeB(double semimajor_axis, double eccentricity, double inclination,  double mean_longitude, double perihelion_longitude, double ascending_node_longitude) {
        super(semimajor_axis, eccentricity, inclination, mean_longitude, perihelion_longitude, ascending_node_longitude, 0);

        double perihelion_argument = perihelion_longitude-ascending_node_longitude;

        double mean_anomaly = mean_longitude-perihelion_longitude;
        mean_anomaly = mean_anomaly % 360;

        double E = mean_anomaly + eccentricity*sin(toRadians(mean_anomaly));

        double deltaE = 1, deltaM;
        while(deltaE > pow(10,-6)) {
            deltaM = mean_anomaly-(E-eccentricity*sin(E));
            deltaE = deltaM/(1-eccentricity*cos(E));
            E = E+deltaE;
        }

        E = toRadians(E);

        double xprime = semimajor_axis*(cos(E)-eccentricity);
        double yprime = semimajor_axis*sqrt(1-eccentricity*eccentricity)*sin(E);

        double r = sqrt(xprime*xprime+yprime*yprime);

        this.velocity = sqrt(G*sunMass*(2/r-1/semimajor_axis));
    }

}

