package cometsim.fastPlanets;

import cometsim.ElementsPlanet;
import cometsim.KnownBody;
import cometsim.PlanetOrbit;

import javax.vecmath.Color3f;

public class Venus extends KnownBody {

    double ti = 2451545;
    double tp=0.62*365;
    double listCoordinates[][] = new double[(int) tp+1][3];
    PlanetOrbit interpolator;

    public Venus() {
        super("Venus", 4.8690E24,
                new ElementsPlanet(0.723321, 0.00676399, 3.39778, 181.98, 131.768, 76.6726, -2.6E-7, -0.00005107, 0.00043494, 58517.8, 0.0567965, -0.272742)
                ,new Color3f(0.5f,0.5f,0.5f));

        for(int t=0; t<tp; t++) {
            listCoordinates[t]=super.coordinates(ti+t);
        }

        interpolator = new PlanetOrbit(listCoordinates,tp);
    }

    public double[] coordinates(double julian) {
        return interpolator.coordinates(julian);
    }
}
