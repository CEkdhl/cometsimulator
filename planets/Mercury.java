package cometsim.planets;

import cometsim.ElementsPlanet;
import cometsim.KnownBody;
import cometsim.PlanetOrbit;

import javax.vecmath.Color3f;

public class Mercury extends KnownBody {

    public Mercury() {
        super("Mercury", 3.3022E23,
                new ElementsPlanet(0.387098, 0.205637, 7.00559, 252.252, 77.4577, 48.3396, 0., 0.00002123, -0.00590158, 149473., 0.1594, -0.122142),
                new Color3f(0.7f,0.5f,0.5f));
    }

}