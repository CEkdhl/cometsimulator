package cometsim.planets;

import cometsim.ElementsPlanet;
import cometsim.KnownBody;

import javax.vecmath.Color3f;

public class Mars extends KnownBody {
    
    public Mars() {
        super("Mars", 6.4191E23,
                new ElementsPlanet(1.52371, 0.0933651, 1.85182, -4.56813, -23.9174, 49.7132, 9.7E-7, 0.00009149, -0.00724757, 19140.3, 0.452236, -0.268524),
                new Color3f(1.0f,0.0f,0.0f));
    }
    
}
