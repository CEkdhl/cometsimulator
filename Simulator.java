package cometsim;

import cometsim.eventHandlers.Stop;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math3.ode.sampling.StepHandler;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class stores information about a simulation, as well as execute it.
 */
public class Simulator implements Serializable {

    private ArrayList<KnownBody> knownBodies = new ArrayList<KnownBody>();
    private ArrayList<UnknownBody> unknownBodies = new ArrayList<UnknownBody>();

    private double minStep = 1.0E-15;
    private double maxStep = 100;
    private double absoluteTolerance = 1.0E-20;
    private double relativeTolerance = 1.0E-20;
    private double t0;
    private double tf;
    private ContinuousOutputModel stepHandler;
    private double timing = 0;
    private ArrayList<EventHandler> eventHandlers = new ArrayList<EventHandler>();
    private boolean mutualGravity = false;
    private boolean continuousOutput = true;
    private ArrayList<StepHandler> stepHandlers = new ArrayList<StepHandler>();
    private ArrayList<ExternalForce> externalForces = new ArrayList<ExternalForce>();
    private double statisticsInterval = 0;
    private double[] finalVal;

    /**
     * Add body to the list of known bodies, i.e. bodies whose paths are known.
     * @param body Known body (KnownBody)
     */
    public void add(KnownBody body) {
        knownBodies.add(body);
    }

    /**
     * Add body to the list of unknown bodies, i.e. bodies whose paths will be simulated.
     * @param body Unknown body (UnknownBody)
     */
    public void add(UnknownBody body) {
        unknownBodies.add(body);
    }

    /**
     * Start a simulation based on the bodies that have so far been added to the simulator object. Stores the result as a step handler in a local variable.
     * @param t0 initial value of the time variable
     * @param tf final value of the time variable
     */
    public void simulate(double t0, double tf) {
        this.t0 = t0;

        FirstOrderIntegrator dp853 = new DormandPrince853Integrator(minStep, maxStep, absoluteTolerance, relativeTolerance);
        FirstOrderDifferentialEquations ode = new Gravity(knownBodies, unknownBodies, externalForces);

        if(mutualGravity) {
            ode = new MutualGravity(knownBodies, unknownBodies, externalForces);
        }

        if(continuousOutput) {
            stepHandler = new ContinuousOutputModel();
            dp853.addStepHandler(stepHandler);
        }

        if(statisticsInterval > 0) {
            KnownBody[] kba = knownBodies.toArray(new KnownBody[knownBodies.size()]);
            UnknownBody[] ukba = unknownBodies.toArray(new UnknownBody[unknownBodies.size()]);
            ExternalForce[] efa = externalForces.toArray(new ExternalForce[externalForces.size()]);
            dp853.addStepHandler(new Statistics(kba, ukba, efa, statisticsInterval));
        }

        for(StepHandler s: stepHandlers) {
            dp853.addStepHandler(s);
        }

        for(EventHandler eh: eventHandlers) {
          dp853.addEventHandler(eh,0.1,0.1,100);
        }

        double[] y = new double[6*unknownBodies.size()];
        for(int i=0; i<unknownBodies.size(); i++) {
            double[] ivs = unknownBodies.get(i).getIVs();
            y[i*6] = ivs[0];
            y[i*6+1] = ivs[1];
            y[i*6+2] = ivs[2];
            y[i*6+3] = ivs[3];
            y[i*6+4] = ivs[4];
            y[i*6+5] = ivs[5];
        }
        double start = System.nanoTime();
        this.tf = dp853.integrate(ode, t0, y, tf, y);
        timing = (System.nanoTime()-start)/1E9;

        this.finalVal = y;
    }

    /**
     * Sets the minimum step size allowed by the ODE solver.
     * @param size Step size (double)
     */
    public void setMinStep(double size) {
        minStep = size;
    }

    /**
     * Sets the maximum step size allowed by the ODE solver.
     * @param size Step size (double)
     */
    public void setMaxStep(double size) {
        maxStep = size;
    }

    /**
     * Set the absolute tolerance used by the ODE solver.
     * @param tol Absolute tolerance (double)
     */
    public void setAbsoluteTolerance(double tol) {
        absoluteTolerance = tol;
    }

    /**
     *
     * @param tol
     */
    public void setRelativeTolerance(double tol) {
        relativeTolerance = tol;
    }

    /**
     * Get the list of known bodies added to the simulator.
     * @return list of known bodies (ArrayList)
     */
    public ArrayList<KnownBody> getKnownBodies() {
        return knownBodies;
    }

    /**
     * Removes all known bodies.
     */
    public void removeKnownBodies() {
        knownBodies.clear();
    }

    /**
     * Removes all unknown bodies.
     */
    public void removeUnknownBodies() {
        unknownBodies.clear();
    }

    /**
     * Get the list of unknown bodies added to the simulator.
     * @return list of unknown bodies (ArrayList)
     */
    public ArrayList<UnknownBody> getUnknownBodies() {
        return unknownBodies;
    }

    /**
     * Get the minimum step size that the ODE solver allowed.
     * @return minimum step size (double)
     */
    public double getMinStep() {
        return minStep;
    }

    /**
     * Get maximum step size that the ODE solver allowed.
     * @return maximum step size (double)
     */
    public double getMaxStep() {
        return maxStep;
    }

    /**
     * Get the absolute tolerance used by the ODE solver.
     * @return absolute tolerance (double)
     */
    public double getAbsoluteTolerance() {
        return absoluteTolerance;
    }

    /**
     * Get the relative tolerance used by the ODE solver.
     * @return relative tolerance (double)
     */
    public double getRelativeTolerance() {
        return relativeTolerance;
    }

    /**
     * Get the time value at which the simulation started.
     * @return initial value of the time variable (double)
     */
    public double getT0() {
        return t0;
    }

    /**
     * Get the time value at which the simulation ended.
     * @return final value of the time variable (double)
     */
    public double getTf() {
        return tf;
    }

    /**
     * Get the step handler, the object that encapsulates the result of the simulation.
     * @return step handler (StepHandler)
     */
    public ContinuousOutputModel getStepHandler() {
        return stepHandler;
    }

    /**
     * Get the number of seconds it took for the last simulation to be calculated.
     * @return time elapsed from simulation start to simulation finish in seconds
     */
    public double getTiming() {
        return timing;
    }

    /**
     * Add an event handler, such as a stopper, to the simulator.
     * @param eventHandler
     */
    public void addEventHandler(EventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }

    /**
     * Add a stop event handler and add to it the list of
     * known bodies.
     * @param eventHandler the stopper
     */
    public void addEventHandler(Stop eventHandler) {
        eventHandler.setKnownBodies(knownBodies);
        eventHandlers.add(eventHandler);
    }

    /**
     * Removes all event handlers.
     */
    public void removeEventHandlers() {
        eventHandlers = new ArrayList<EventHandler>();
    }

    /**
     * Add a step handler.
     * @param s StepHandler
     */
    public void addStepHandler(StepHandler s) {
        stepHandlers.add(s);
    }

    /**
     * Remove all step handlers, except for ContinuousOutput if that is enabled.
     */
    public void removeStepHandlers() {
        stepHandlers = new ArrayList<StepHandler>();
    }

    /**
     * Whether unknown bodies affect each other.
     * @param mutual true or false
     */
    public void setMutualGravity(boolean mutual) {
        this.mutualGravity = mutual;
    }

    /**
     * Whether or not the integrator is set to create a continuous output model.
     * @param continuousOutput True or false
    */
    public void setContinuousOutput(boolean continuousOutput) {
        this.continuousOutput = continuousOutput;
    }

    /**
     * Whether or not the integrator is set to create a continuous output model.
     */
    public boolean getContinousOutput() {
        return this.continuousOutput;
    }

    /**
     * Add an external force object to the simulator.
     * @param force External force
     */
    public void addExternalForce(ExternalForce force) {
        externalForces.add(force);
    }

    /**
     * Sets how often to log statistics
     * @param interval number of days between each instance of logged data
     */
    public void setStatisticsInterval(double interval) {
        this.statisticsInterval = interval;
    }

    /**
     * Returns the last y value of the simulation.
     * @return final y values (double)
     */
    public double[] getFinalValue() {
        return this.finalVal;
    }
}
