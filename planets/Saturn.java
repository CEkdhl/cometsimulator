package cometsim.planets;

import cometsim.ElementsPlanet;
import cometsim.KnownBody;

import javax.vecmath.Color3f;

public class Saturn extends KnownBody {
    
    public Saturn() {
        super("Saturn", 5.6850E26,
                new ElementsPlanet(9.5415, 0.0555083, 2.49424, 50.0757, 92.8614, 113.64, -0.00003065, -0.00032044, 0.00451969, 1222.11, 0.541795, -0.25015),
                new Color3f(1.0f,0.5f,0.0f));
    }
    
}
