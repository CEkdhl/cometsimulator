package cometsim.planets;

import cometsim.Constants;
import cometsim.KnownBody;
import cometsim.XYZCoordinates;

public class Sun extends KnownBody{

    public Sun() {
        super("Sun", Constants.sunMass, new XYZCoordinates(0,0,0,0,0,0));
    }

}
