package cometsim;

import javax.vecmath.Color3f;
import java.io.Serializable;
import static cometsim.Constants.*;
import static java.lang.Math.*;

public class Body implements Serializable, Cloneable {
    
    private String name;
    private double mass;
    private OrbitalElements elements;
    private Color3f color = new Color3f(1.0f,1.0f,0.0f);
    private boolean isLightSource;
    
    /**
     * Basic constructor.
     * @param name Name of the body. Acts as an identifier.
     * @param mass Weight of the body in kg.
     * @param elements  Object which encapsulates the orbital elements that defines the state of the object.
     */
    public Body(String name, double mass, OrbitalElements elements) {
        this.name = name;
        this.mass = mass;
        this.elements = elements;
    }
    
    /**
     * Basic constructor.
     * @param name Name of the body. Acts as an identifier.
     * @param mass Weight of the body in kg.
     * @param elements  Object which encapsulates the orbital elements that defines the state of the object.
     * @param color The color of the object.
     */
    public Body(String name, double mass, OrbitalElements elements, Color3f color) {
        this.name = name;
        this.mass = mass;
        this.elements = elements;
        this.color=color;
    }
    /**
     * Get the ID/name of this body.
     * @return ID (String)
     */
    public String getID() {
        return name;
    }
    
    /**
     * Get the mass of this body.
     * @return mass (double)
     */
    public double getMass() {
        return mass;
    }
    
    /**
     * Set the color of the body.
     * @param c The color (Color3f)
     */
    public void setColor(Color3f c) {
        color = c;
    }
    
    
    /**
     * Gets the color of the body.
     * @return color (Color3f)
     */
    public Color3f getColor() {
        return color;
    }
    
    /**
     * Gets the coordinates of the body.
     * @param julian Julian date.
     * @return coordinates (double[3])
     */
    public double[] coordinates(double julian) {
        return elements.coordinates(julian);
    }
    
    /**
     * Gets the initial state vector corresponding to the given orbital elements.
     * @return initial values (double[])
     */
    public double[] getIVs() {
        return elements.getIVs();
    }

    /**
     * Gets the initial state vector corresponding to the given orbital elements.
     * @param julian Julian date.
     * @return initial values (double[])
     */
    public double[] getIVs(double julian) {
        return elements.getIVs();
    }
    
    /**
     * Whether this body emits light. Used for visualization purposes.
     * @param isLightSource True or false
     */
    public void setLightSource(boolean isLightSource) {
        this.isLightSource = isLightSource;
    }
    
    /**
     * Get whether this body emits light or not.
     * @return True or false
     */
    public boolean isLightSource() {
        return isLightSource;
    }

    /**
     * This object can be cloned.
     * @return A copy of itself
     * @throws CloneNotSupportedException
     */
    public Body clone() throws CloneNotSupportedException {
        return (Body)super.clone();
    }

    /**
     * Replace the current set of elements of this body.
     * @param elements Orbital elements
     */
    public void setElements(OrbitalElements elements) {
        this.elements = elements;
    }
}
