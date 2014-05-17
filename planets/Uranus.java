package cometsim.planets;

import cometsim.ElementsPlanet;
import cometsim.KnownBody;

import javax.vecmath.Color3f;

public class Uranus extends KnownBody {
    
    public Uranus() {
        super("Uranus", 8.6625E25,
                new ElementsPlanet(19.188, 0.0468574, 0.772981, 314.203, 172.434, 73.9625, -0.00020455, -0.0000155, -0.00180155, 428.495, 0.0926699, 0.057397),
                new Color3f(0.0f, 0.5f, 0.5f));
    }
    
}
