package cometsim.fastPlanets;

import cometsim.ElementsPlanet;
import cometsim.KnownBody;
import cometsim.PlanetOrbit;

import javax.vecmath.Color3f;

public class Neptune extends KnownBody {

    double ti = 2451545;
    double tp=164.79*365.25;
    double listCoordinates[][] = new double[(int) tp+1][3];
    PlanetOrbit interpolator;

    public Neptune() {
        super("Neptune", 1.0278E26,
                new ElementsPlanet(30.0695, 0.00895439, 1.77006, 304.223, 46.6816, 131.786, 0.00006447, 8.18E-6, 0.000224, 218.465, 0.0100994, -0.00606302),
                new Color3f(0.0f, 0.5f,1.0f));

        for(int t=0; t<tp; t++) {
            listCoordinates[t]=super.coordinates(ti+t);
        }

        interpolator = new PlanetOrbit(listCoordinates,tp);
    }

    public double[] coordinates(double julian) {
        return interpolator.coordinates(julian);
    }
}