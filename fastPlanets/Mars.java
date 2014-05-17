package cometsim.fastPlanets;

import cometsim.ElementsPlanet;
import cometsim.KnownBody;
import cometsim.PlanetOrbit;

import javax.vecmath.Color3f;

public class Mars extends KnownBody {

    double ti = 2451545;
    double tp=1.88*365.25;
    double listCoordinates[][] = new double[(int) tp+1][3];
    PlanetOrbit interpolator;

    public Mars() {
        super("Mars", 6.4191E23,
                new ElementsPlanet(1.52371, 0.0933651, 1.85182, -4.56813, -23.9174, 49.7132, 9.7E-7, 0.00009149, -0.00724757, 19140.3, 0.452236, -0.268524),
                new Color3f(1.0f,0.0f,0.0f));

        for(int t=0; t<tp; t++) {
            listCoordinates[t]=super.coordinates(ti+t);
        }

        interpolator = new PlanetOrbit(listCoordinates,tp);
    }

    public double[] coordinates(double julian) {
        return interpolator.coordinates(julian);
    }
}
