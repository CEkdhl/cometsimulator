package cometsim;

import cometsim.*;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.FastMath;

import java.io.PrintWriter;

public class Statistics implements StepHandler {

    KnownBody[] knownBodies;
    UnknownBody[] unknownBodies;
    ExternalForce[] forceFields;
    PrintWriter writer;

    double t0;
    double lastT;
    double interval;
    double[] com0 = new double[3];
    double totalMass = 0;

    public Statistics(KnownBody[] knownBodies, UnknownBody[] unknownBodies, ExternalForce[] forceFields, double interval) {
        this.knownBodies = knownBodies;
        this.unknownBodies = unknownBodies;
        this.forceFields = forceFields;
        this.interval = interval;

        for(UnknownBody b: unknownBodies) {
            totalMass+=b.getMass();
        }

        try{
            writer = new PrintWriter("statistics.txt");
        } catch(Exception e) { e.printStackTrace(); }
    }

    public double kineticEnergy(double[] y) {
        double kineticEnergy = 0;
        for(int i=0; i<y.length; i+=6) {
            kineticEnergy+=unknownBodies[i/6].getMass()*(y[i+3]*y[i+3]+y[i+4]*y[i+4]+y[i+5]*y[i+5])/2;
        }
        return kineticEnergy;
    }

    public double potentialEnergy(double t, double[] y) {
        double potentialEnergy = 0;
        for(int i=0; i<y.length; i+=6) {
            for(int j=0; j<y.length; j+=6) {
                if(i==j) continue;

                double m1 = unknownBodies[i/6].getMass();
                double m2 = unknownBodies[j/6].getMass();
                double G = Constants.G;
                double deltax = y[i]-y[j];
                double deltay = y[i+1]-y[j+1];
                double deltaz = y[i+2]-y[j+2];

                potentialEnergy+= -0.5*G*m1*m2/FastMath.sqrt(deltax * deltax + deltay * deltay + deltaz * deltaz);
            }

            for(ExternalForce f: forceFields) {
                if(f instanceof HasPotential) {
                    double[] stateVector = {y[i],y[i+1],y[i+2]};
                    potentialEnergy+=((HasPotential) f).getPotential(t, stateVector, unknownBodies[i/6]);
                }
            }
        }

        return potentialEnergy;
    }

    public double meanDistance(double[] y) {
        double[][] R = new double[y.length/6][y.length/6];
        for(int i=0; i<y.length/6; i++) {
            for(int j=i; j<y.length/6; j++) {
                double deltax = y[6*i]-y[6*j];
                double deltay = y[6*i+1]-y[6*j+1];
                double deltaz = y[6*i+2]-y[6*j+2];
                R[i][j]=FastMath.sqrt(deltax*deltax+deltay*deltay+deltaz*deltaz);
            }
        }

        double totDistance = 0;
        for(int i=0; i<y.length/6; i++) {
            for(int j=0; j<y.length/6; j++) {
                totDistance+=R[i][j];
            }
        }

        return 36*totDistance/(y.length*y.length);
    }

    public int numberOfBoundStars(double[] y) {
        int boundStars = 0;
        for(int i=0; i<y.length; i+=6) {
            double kinEn = 0.5*unknownBodies[i/6].getMass()*(y[i+3]*y[i+3]+y[i+4]*y[i+4]+y[i+5]*y[i+5]);
            double potEn = 0;
            for(int j=0; j<y.length; j+=6) {
                if(i==j) continue;

                double m1 = unknownBodies[i/6].getMass();
                double m2 = unknownBodies[j/6].getMass();
                double G = Constants.G;
                double deltax = y[i]-y[j];
                double deltay = y[i+1]-y[j+1];
                double deltaz = y[i+2]-y[j+2];

                potEn+= -G*m1*m2/FastMath.sqrt(deltax * deltax + deltay * deltay + deltaz * deltaz);
            }
            if(kinEn+potEn < 0) boundStars++;
        }
        return boundStars;
    }

    public double deltaCenterOfMass(double[] y) {

        double[] com = new double[3];
        for(int i=0; i<y.length/6; i++) {
            com[0]+=y[6*i]*unknownBodies[i].getMass();
            com[1]+=y[6*i+1]*unknownBodies[i].getMass();
            com[2]+=y[6*i+2]*unknownBodies[i].getMass();
        }

        double deltax = (com0[0]-com[0])/totalMass;
        double deltay = (com0[1]-com[1])/totalMass;
        double deltaz = (com0[2]-com[2])/totalMass;

        return FastMath.sqrt(deltax*deltax+deltay*deltay+deltaz*deltaz);
    }

    public void init(double t0, double[] y0, double t) {
        this.t0 = t0;
        this.lastT = t0;

        double[] com = new double[3];
        for(int i=0; i<y0.length/6; i++) {
            com[0]+=y0[6*i]*unknownBodies[i].getMass();
            com[1]+=y0[6*i+1]*unknownBodies[i].getMass();
            com[2]+=y0[6*i+2]*unknownBodies[i].getMass();
        }
        com0 = com;
    }

    public void handleStep(StepInterpolator interpolator, boolean isLast) throws MaxCountExceededException {
        double   t = interpolator.getCurrentTime();
        double[] y = interpolator.getInterpolatedState();

        if(t > lastT+interval) {
            writer.println(t+" "+(potentialEnergy(t, y)+kineticEnergy(y))+" " + potentialEnergy(t, y) + " " + kineticEnergy(y)+" "+kineticEnergy(y)/FastMath.abs(potentialEnergy(t, y))+" "+meanDistance(y)+" "+numberOfBoundStars(y)+" "+deltaCenterOfMass(y));
            lastT = t;
        }
    }
}
