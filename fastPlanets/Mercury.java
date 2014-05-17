package cometsim.fastPlanets;

import cometsim.ElementsPlanet;
import cometsim.KnownBody;
import cometsim.PlanetOrbit;

import javax.vecmath.Color3f;

public class Mercury extends KnownBody {

    double ti = 2451545;
    double tp=0.24*365.25;
    double listCoordinates[][] = new double[(int) tp+1][3];
    PlanetOrbit interpolator;

    public Mercury() {
        super("Mercury", 3.3022E23,
                new ElementsPlanet(0.387098, 0.205637, 7.00559, 252.252, 77.4577, 48.3396, 0., 0.00002123, -0.00590158, 149473., 0.1594, -0.122142),
                new Color3f(0.7f,0.5f,0.5f));

        for(int t=0; t<tp; t++) {
            listCoordinates[t]=super.coordinates(ti+t);
        }

        interpolator = new PlanetOrbit(listCoordinates,tp);
    }

    public double[] coordinates(double julian) {
        return interpolator.coordinates(julian);
    }
}
