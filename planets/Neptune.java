package cometsim.planets;

import cometsim.ElementsPlanet;
import cometsim.KnownBody;

import javax.vecmath.Color3f;

public class Neptune extends KnownBody {
    
    public Neptune() {
        super("Neptune", 1.0278E26,
                new ElementsPlanet(30.0695, 0.00895439, 1.77006, 304.223, 46.6816, 131.786, 0.00006447, 8.18E-6, 0.000224, 218.465, 0.0100994, -0.00606302),
                new Color3f(0.0f, 0.5f,1.0f));
    }
    
}
