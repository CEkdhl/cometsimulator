package cometsim.fastPlanets;

import cometsim.ElementsPlanet;
import cometsim.KnownBody;
import cometsim.PlanetOrbit;

import javax.vecmath.Color3f;

public class Uranus extends KnownBody {

    double ti = 2451545;
    double tp=84.02*365.25;
    double listCoordinates[][] = new double[(int) tp+1][3];
    PlanetOrbit interpolator;

    public Uranus() {
        super("Uranus", 8.6625E25,
                new ElementsPlanet(19.188, 0.0468574, 0.772981, 314.203, 172.434, 73.9625, -0.00020455, -0.0000155, -0.00180155, 428.495, 0.0926699, 0.057397),
                new Color3f(0.0f, 0.5f, 0.5f));

        for(int t=0; t<tp; t++) {
            listCoordinates[t]=super.coordinates(ti+t);
        }

        interpolator = new PlanetOrbit(listCoordinates,tp);
    }

    public double[] coordinates(double julian) {
        return interpolator.coordinates(julian);
    }
}