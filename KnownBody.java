package cometsim;

import javax.vecmath.Color3f;

public class KnownBody extends Body {
    /**
     * Basic constructor.
     * @param name Name of the body. Acts as an identifier.
     * @param mass Weight of the body in kg.
     * @param elements  Object which encapsulates the orbital elements that defines the state of the object.
     */
    public KnownBody(String name, double mass, OrbitalElements elements) {
        super(name, mass, elements);
    }
    
    /**
     * Basic constructor.
     * @param name Name of the body. Acts as an identifier.
     * @param mass Weight of the body in kg.
     * @param elements  Object which encapsulates the orbital elements that defines the state of the object.
     * @param color The color of the object.
     */
    public KnownBody(String name, double mass, OrbitalElements elements, Color3f color) {
        super(name, mass, elements, color);
    }
}
