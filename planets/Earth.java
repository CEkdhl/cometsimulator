package cometsim.planets;

import cometsim.ElementsPlanet;
import cometsim.KnownBody;

import javax.vecmath.Color3f;

public class Earth extends KnownBody {
    
    public Earth() {
        super("Earth", 5.9721986E24,
                new ElementsPlanet(1., 0.0167316, -0.00054346, 100.467, 102.93, -5.1126, -3.0E-8, -0.00003661, -0.0133718, 35999.4, 0.317953, -0.241239),
                new Color3f(0.0f,0.2f,1.0f));
    }
    
}
