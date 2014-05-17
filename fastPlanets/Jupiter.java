package cometsim.fastPlanets;

import cometsim.ElementsPlanet;
import cometsim.KnownBody;
import cometsim.PlanetOrbit;

import javax.vecmath.Color3f;

public class Jupiter extends KnownBody {

    double ti = 2451545;
    double tp=11.86*365.25;
    double listCoordinates[][] = new double[(int) tp+1][3];
    PlanetOrbit interpolator;

    public Jupiter() {
        super("Jupiter",
                1.8988E27,
                new ElementsPlanet(5.20248, 0.0485359, 1.29861, 34.3348, 14.275, 100.293, -0.00002864, 0.00018026, -0.00322699, 3034.9, 0.181992, 0.130246),
                new Color3f(0.5f,0.5f,0.1f)
        );

        for(int t=0; t<tp; t++) {
            listCoordinates[t]=super.coordinates(ti+t);
        }

        interpolator = new PlanetOrbit(listCoordinates,tp);
    }

    public double[] coordinates(double julian) {
        return interpolator.coordinates(julian);
    }

}
