package cometsim;

import cometsim.OrbitalElements;

import static java.lang.Math.*;

/**
 * ElementsTypeA uses the orbital elements used for planets, but instead of accepting
 * the rate of change of these elements in order to calculate velocity it uses a
 * parameter for velocity in the cartesian coordinate system tangent to the path
 * given by the orbital elements.
 */
public class ElementsTypeA extends OrbitalElements {

    public final double semimajor_axis;
    public final double eccentricity;
    public final double inclination;
    public final double mean_longitude;
    public final double perihelion_longitude;
    public final double ascending_node_longitude;
    public double velocity;

    /**
     * Basic constructor.
     * @param semimajor_axis
     * @param eccentricity
     * @param inclination
     * @param mean_longitude
     * @param perihelion_longitude
     * @param ascending_node_longitude
     * @param velocity
     */
    public ElementsTypeA(double semimajor_axis, double eccentricity, double inclination,  double mean_longitude, double perihelion_longitude, double ascending_node_longitude, double velocity ) {
        this.semimajor_axis = semimajor_axis;
        this.eccentricity = eccentricity;
        this.inclination = inclination;
        this.mean_longitude = mean_longitude;
        this.perihelion_longitude = perihelion_longitude;
        this.ascending_node_longitude = ascending_node_longitude;
        this.velocity = velocity;
    }

    public double[] coordinates(double julian) {
        /*
         * Detta är inte implementerat ännu men det borde implementeras.
         * Tanken är att man ska kunna både simulera en kropp och samtidigt
         * begära hur den skulle ha åkt om den skulle ha färdats som i tvåkroppsproblemet
         */

        return new double[3];
    }

    /**
     * Calculates the initial state vector based on the orbital elements given.
     * @return State vector {x,y,z,dx,dy,dz} (double[])
     */
    public double[] getIVs() {
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
        perihelion_argument = toRadians(perihelion_argument);
        double ascending_node_longitude = toRadians(this.ascending_node_longitude);
        double inclination = toRadians(this.inclination);

        double xprime = semimajor_axis*(cos(E)-eccentricity);
        double yprime = semimajor_axis*sqrt(1-pow(eccentricity,2))*sin(E);

        double x = (cos(perihelion_argument)*cos(ascending_node_longitude) - sin(perihelion_argument)*sin(ascending_node_longitude)*cos(inclination))*xprime+(-sin(perihelion_argument)*cos(ascending_node_longitude) - cos(perihelion_argument)*sin(ascending_node_longitude)*cos(inclination))*yprime;
        double y = (cos(perihelion_argument)*sin(ascending_node_longitude) + sin(perihelion_argument)*cos(ascending_node_longitude)*cos(inclination))*xprime+(-sin(perihelion_argument)*sin(ascending_node_longitude) + cos(perihelion_argument)*cos(ascending_node_longitude)*cos(inclination))*yprime;
        double z = sin(perihelion_argument)*sin(inclination)*xprime+(cos(perihelion_argument)*sin(inclination))*yprime;

        double[] coordinates = {x,y,z};

        double xt = -semimajor_axis*sin(E);
        double yt = semimajor_axis*sqrt(1-pow(eccentricity,2))*cos(E);

        double dx = (cos(perihelion_argument)*cos(ascending_node_longitude) - sin(perihelion_argument)*sin(ascending_node_longitude)*cos(inclination))*xt+(-sin(perihelion_argument)*cos(ascending_node_longitude) - cos(perihelion_argument)*sin(ascending_node_longitude)*cos(inclination))*yt;
        double dy = (cos(perihelion_argument)*sin(ascending_node_longitude) + sin(perihelion_argument)*cos(ascending_node_longitude)*cos(inclination))*xt+(-sin(perihelion_argument)*sin(ascending_node_longitude) + cos(perihelion_argument)*cos(ascending_node_longitude)*cos(inclination))*yt;
        double dz = sin(perihelion_argument)*sin(inclination)*xt+(cos(perihelion_argument)*sin(inclination))*yt;

        double norm = sqrt((pow(dx, 2) + pow(dy, 2) + pow(dz,2)));
        dx = dx/norm;
        dy = dy/norm;
        dz = dz/norm;

        double[] IVs = new double[6];

        // First three initial values are x, y, z
        IVs[0] = coordinates[0];
        IVs[1] = coordinates[1];
        IVs[2] = coordinates[2];

        // Last three initial values are dx/dt, dy/dt, dz/dt
        IVs[3] = velocity*dx;
        IVs[4] = velocity*dy;
        IVs[5] = velocity*dz;

        // Ev. kan man använda mean motion istället för hastighet

        return IVs;
    }

}
