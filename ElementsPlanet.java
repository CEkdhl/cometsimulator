package cometsim;

import cometsim.OrbitalElements;

import static java.lang.Math.*;

/**
 * This class extends OrbitalElements. It accepts the set of orbital elements commonly used for major planets. Given the rate of change of parameters it can determine the position in xyz coordinates.
 * The coordinate transformation is based on the procedure described <a href="http://ssd.jpl.nasa.gov/?planet_pos">here</a>.
 */
public class ElementsPlanet extends OrbitalElements {

    private final double semimajor_axis;
    private final double eccentricity;
    private final double inclination;
    private final double mean_longitude;
    private final double perihelion_longitude;
    private final double ascending_node_longitude;
    private final double semimajor_axis_v;
    private final double eccentricity_v;
    private final double inclination_v;
    private final double mean_longitude_v;
    private final double perihelion_longitude_v;
    private final double ascending_node_longitude_v;
    private final double b;
    private final double c;
    private final double s;
    private final double f;

    /**
     *
     * Constructor that accepts the set of orbital elements commonly used for major planets, as well as their rates of change and planetary perturbation parameters.
     *
     * @param semimajor_axis Semimajor axis
     * @param eccentricity Eccentricity
     * @param inclination Inclination
     * @param mean_longitude Mean longitude
     * @param perihelion_longitude Perihelion longitude
     * @param ascending_node_longitude Ascending node longitude
     * @param semimajor_axis_v Semimajor axis rate of change
     * @param eccentricity_v Eccentricity rate of change
     * @param inclination_v Inclination rate of change
     * @param mean_longitude_v Mean longitude rate of change
     * @param perihelion_longitude_v Perihelion longitude rate of change
     * @param ascending_node_longitude_v Ascending node longitude rate of change
     * @param b Perturbation parameter b
     * @param c Perturbation parameter c
     * @param s Perturbation parameter s
     * @param f Perturbation parameter f
     */
    public ElementsPlanet(double semimajor_axis, double eccentricity, double inclination, double mean_longitude, double perihelion_longitude, double ascending_node_longitude, double semimajor_axis_v, double eccentricity_v, double inclination_v, double mean_longitude_v, double perihelion_longitude_v, double ascending_node_longitude_v, double b, double c, double s, double f) {
        this.semimajor_axis = semimajor_axis;
        this.eccentricity = eccentricity;
        this.inclination = inclination;
        this.mean_longitude = mean_longitude;
        this.perihelion_longitude = perihelion_longitude;
        this.ascending_node_longitude = ascending_node_longitude;
        this.semimajor_axis_v = semimajor_axis_v;
        this.eccentricity_v = eccentricity_v;
        this.inclination_v = inclination_v;
        this.mean_longitude_v = mean_longitude_v;
        this.perihelion_longitude_v = perihelion_longitude_v;
        this.ascending_node_longitude_v = ascending_node_longitude_v;
        this.b = b;
        this.c = c;
        this.s = s;
        this.f = f;
    }

    /**
     *
     * Constructor that accepts the set of orbital elements commonly used for major planets, as well as their rates of change. Sets planetary perturbation parameters to zero.
     *
     * @param semimajor_axis Semimajor axis
     * @param eccentricity Eccentricity
     * @param inclination Inclination
     * @param mean_longitude Mean longitude
     * @param perihelion_longitude Perihelion longitude
     * @param ascending_node_longitude Ascending node longitude
     * @param semimajor_axis_v Semimajor axis rate of change
     * @param eccentricity_v Eccentricity rate of change
     * @param inclination_v Inclination rate of change
     * @param mean_longitude_v Mean longitude rate of change
     * @param perihelion_longitude_v Perihelion longitude rate of change
     * @param ascending_node_longitude_v Ascending node longitude rate of change
     */
    public ElementsPlanet(double semimajor_axis, double eccentricity, double inclination, double mean_longitude, double perihelion_longitude, double ascending_node_longitude, double semimajor_axis_v, double eccentricity_v, double inclination_v, double mean_longitude_v, double perihelion_longitude_v, double ascending_node_longitude_v) {
        this.semimajor_axis = semimajor_axis;
        this.eccentricity = eccentricity;
        this.inclination = inclination;
        this.mean_longitude = mean_longitude;
        this.perihelion_longitude = perihelion_longitude;
        this.ascending_node_longitude = ascending_node_longitude;
        this.semimajor_axis_v = semimajor_axis_v;
        this.eccentricity_v = eccentricity_v;
        this.inclination_v = inclination_v;
        this.mean_longitude_v = mean_longitude_v;
        this.perihelion_longitude_v = perihelion_longitude_v;
        this.ascending_node_longitude_v = ascending_node_longitude_v;
        this.b = 0;
        this.c = 0;
        this.s = 0;
        this.f = 0;
    }

    /**
     *
     * Constructor that accepts the set of orbital elements commonly used for major planets. Sets the corresponding rates of change and the planetary perturbation parameters to zero.
     *
     * @param semimajor_axis Semimajor axis
     * @param eccentricity Eccentricity
     * @param inclination Inclination
     * @param mean_longitude Mean longitude
     * @param perihelion_longitude Perihelion longitude
     * @param ascending_node_longitude Ascending node longitude
     */
    public ElementsPlanet(double semimajor_axis, double eccentricity, double inclination, double mean_longitude, double perihelion_longitude, double ascending_node_longitude) {
        this.semimajor_axis = semimajor_axis;
        this.eccentricity = eccentricity;
        this.inclination = inclination;
        this.mean_longitude = mean_longitude;
        this.perihelion_longitude = perihelion_longitude;
        this.ascending_node_longitude = ascending_node_longitude;
        this.semimajor_axis_v = 0;
        this.eccentricity_v = 0;
        this.inclination_v = 0;
        this.mean_longitude_v = 0;
        this.perihelion_longitude_v = 0;
        this.ascending_node_longitude_v = 0;
        this.b = 0;
        this.c = 0;
        this.s = 0;
        this.f = 0;
    }

    /**
     * Calculates the xyz position relative to the reference plane on a given date.
     * @param julian Julian day
     * @return xyz coordinates (double[])
     */
    public double[] coordinates(double julian) {
        double T = (julian-2451545)/36525; // 36525 == number of days in a century, 2451545 == number of Julian days up to the J200 epoch January 1, 2000.
        double semimajor_axis = this.semimajor_axis+T*this.semimajor_axis_v;
        double eccentricity = this.eccentricity+T*this.eccentricity_v;
        double inclination = this.inclination+T*this.inclination_v;
        double mean_longitude = this.mean_longitude+T*this.mean_longitude_v;
        double perihelion_longitude = this.perihelion_longitude+T*this.perihelion_longitude_v;
        double ascending_node_longitude = this.ascending_node_longitude+T*ascending_node_longitude_v;

        double perihelion_argument = perihelion_longitude-ascending_node_longitude;

        double mean_anomaly = mean_longitude-perihelion_longitude+b*T*T+c*cos(f * T)+s*sin(f * T);
        mean_anomaly = mean_anomaly % 360;

        double E = mean_anomaly + eccentricity*sin(toRadians(mean_anomaly));

        double deltaE = 1, deltaM;
        while(deltaE > pow(10,-6)) {
            deltaM = mean_anomaly-(E-eccentricity*sin(E));
            deltaE = deltaM/(1-eccentricity*cos(E));
            E = E+deltaE;
        }

        E = toRadians(E);
        perihelion_argument = toRadians(perihelion_argument);
        ascending_node_longitude = toRadians(ascending_node_longitude);
        inclination = toRadians(inclination);

        double xprime = semimajor_axis*(cos(E)-eccentricity);
        double yprime = semimajor_axis*sqrt(1-eccentricity*eccentricity)*sin(E);

        double x = (cos(perihelion_argument)*cos(ascending_node_longitude) - sin(perihelion_argument)*sin(ascending_node_longitude)*cos(inclination))*xprime+(-sin(perihelion_argument)*cos(ascending_node_longitude) - cos(perihelion_argument)*sin(ascending_node_longitude)*cos(inclination))*yprime;
        double y = (cos(perihelion_argument)*sin(ascending_node_longitude) + sin(perihelion_argument)*cos(ascending_node_longitude)*cos(inclination))*xprime+(-sin(perihelion_argument)*sin(ascending_node_longitude) + cos(perihelion_argument)*cos(ascending_node_longitude)*cos(inclination))*yprime;
        double z = sin(perihelion_argument)*sin(inclination)*xprime+(cos(perihelion_argument)*sin(inclination))*yprime;

        double[] coordinates = {x,y,z};

        return coordinates;
    }

    /**
     * Calculates the initial state vector based on the orbital elements given.
     * @return State vector {x,y,z,dx,dy,dz} (double[])
     */
    public double[] getIVs() {
        double[] c0 = coordinates(0);
        double[] c1 = coordinates(1);

        double[] ivs = new double[6];
        ivs[0] = c0[0];
        ivs[1] = c0[1];
        ivs[2] = c0[2];
        ivs[3] = c1[0]-c0[0]; // Just an approximation
        ivs[4] = c1[1]-c0[1];
        ivs[5] = c1[2]-c0[2];

        return ivs;
    }

    /**
     * Calculates the initial state vector based on the orbital elements given.
     * @param julian Julian day.
     * @return State vector {x,y,z,dx,dy,dz} (double[])
     */
    public double[] getIVs(double julian) {
        double[] c0 = coordinates(julian);
        double[] c1 = coordinates(julian+1);

        double[] ivs = new double[6];
        ivs[0] = c0[0];
        ivs[1] = c0[1];
        ivs[2] = c0[2];
        ivs[3] = c1[0]-c0[0]; // Just an approximation
        ivs[4] = c1[1]-c0[1];
        ivs[5] = c1[2]-c0[2];

        return ivs;
    }
}
