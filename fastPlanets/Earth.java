package cometsim.fastPlanets;

import cometsim.ElementsPlanet;
import cometsim.KnownBody;
import cometsim.PlanetOrbit;

import javax.vecmath.Color3f;

public class Earth extends KnownBody {

    double ti = 2451545;
    double tp=365.25;
    double listCoordinates[][] = new double[(int) tp+1][3];
    PlanetOrbit interpolator;

    public Earth() {
        super("Earth", 5.9721986E24,
                new ElementsPlanet(1., 0.0167316, -0.00054346, 100.467, 102.93, -5.1126, -3.0E-8, -0.00003661, -0.0133718, 35999.4, 0.317953, -0.241239),
                new Color3f(0.0f,0.2f,1.0f));

        for(int t=0; t<tp; t++) {
            listCoordinates[t]=super.coordinates(ti+t);
        }

        interpolator = new PlanetOrbit(listCoordinates,tp);
    }

    public double[] coordinates(double julian) {
        return interpolator.coordinates(julian);
    }

}
