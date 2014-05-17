package cometsim;

public class UnknownBody extends Body {
    /**
     * Basic constructor.
     * @param name Name of the body. Acts as an identifier.
     * @param mass Weight of the body in kg.
     * @param elements  Object which encapsulates the orbital elements that defines the state of the object.
     */
    public UnknownBody(String name, double mass, OrbitalElements elements) {
        super(name, mass, elements);
    }
}
