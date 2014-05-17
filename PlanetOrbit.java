package cometsim;

import org.apache.commons.math3.util.FastMath;

public class PlanetOrbit {

    final double[][] coordinates;
    double tp;
    double[] store = new double[3];

    public PlanetOrbit(double[][] coordinates, double tp) {
        this.coordinates = coordinates;
        this.tp = tp;
    }

    public double[] coordinates(double julian){
        int startIndex=(int) (julian>=coordinates.length?1:julian+1);
        int endIndex=(startIndex+1>=coordinates.length?1:startIndex+1);

        double nonIntegerPortion=julian-startIndex;


        double[] start = coordinates[startIndex];
        double[] end = coordinates[endIndex];

        double[] returnPosition= store;

        for(int i=0;i< start.length;i++){
            returnPosition[i]=start[i]*(1-nonIntegerPortion)+end[i]*nonIntegerPortion;
        }
        return returnPosition;
    }
}
