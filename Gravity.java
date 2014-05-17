package cometsim;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import static org.apache.commons.math3.util.FastMath.*;
import static cometsim.Constants.*;

import java.util.ArrayList;

public class Gravity implements FirstOrderDifferentialEquations {

    private ArrayList<KnownBody> knownBodies;
    private ArrayList<UnknownBody> unknownBodies;
    private ArrayList<ExternalForce> externalForces;

    /**
     * Basic constructor
     * @param knownBodies A list of bodies that can affect other bodies and have a known path.
     * @param unknownBodies A list of bodies that are affected by other bodies, but not each other, and which will be simulated.
     */
    public Gravity(ArrayList<KnownBody> knownBodies, ArrayList<UnknownBody> unknownBodies, ArrayList<ExternalForce> externalForces) {
        this.knownBodies = knownBodies;
        this.unknownBodies = unknownBodies;
        this.externalForces = externalForces;
    }

    public Gravity(ArrayList<KnownBody> knownBodies, ArrayList<UnknownBody> unknownBodies) {
        this.knownBodies = knownBodies;
        this.unknownBodies = unknownBodies;
        this.externalForces = new ArrayList<ExternalForce>();
    }

    /**
     * Get the dimension of the system of differential equations.
     * @return dimension (int)
     */
    public int getDimension() {
        return 6*unknownBodies.size();
    }

    public void computeDerivatives(double t, double[] y, double[] yp) {

        double[][][] distance = new double[knownBodies.size()][3*unknownBodies.size()][3];
        double[] coordinates;

        for(int i=0; i<knownBodies.size(); i++) {
            coordinates = knownBodies.get(i).coordinates(t);
            for(int j=0; j<unknownBodies.size(); j++) {
                distance[i][j][0] = coordinates[0]-y[6*j];
                distance[i][j][1] = coordinates[1]-y[6*j+1];
                distance[i][j][2] = coordinates[2]-y[6*j+2];
            }
        }

        for(int i=0; i<unknownBodies.size(); i++) {
            yp[6*i] = y[6*i+3];
            yp[6*i+1] = y[6*i+4];
            yp[6*i+2] = y[6*i+5];
            yp[6*i+3] = 0;
            yp[6*i+4] = 0;
            yp[6*i+5] = 0;
            for(int j=0; j<knownBodies.size(); j++) {
                double coeff = G*knownBodies.get(j).getMass()/pow(distance[j][i][0]*distance[j][i][0]+distance[j][i][1]*distance[j][i][1]+distance[j][i][2]*distance[j][i][2],1.5);
                yp[6*i+3] += coeff*distance[j][i][0];
                yp[6*i+4] += coeff*distance[j][i][1];
                yp[6*i+5] += coeff*distance[j][i][2];
            }

            for(ExternalForce f: externalForces) {
                double[] state = {y[6*i], y[6*i+1],y[6*i+2], y[6*i+3], y[6*i+4], y[6*i+5]};
                double[] acc = f.getAcceleration(t, state, unknownBodies.get(i));

                yp[6*i+3] += acc[0];
                yp[6*i+4] += acc[1];
                yp[6*i+5] += acc[2];
            }

        }

    }
}
