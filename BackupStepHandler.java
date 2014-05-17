package cometsim;

import cometsim.saveAndLoad.HandleIVs;
import cometsim.saveAndLoad.IVs;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class BackupStepHandler implements StepHandler {

    double interval;
    double t0;
    String name;
    double lastT;
    ArrayList<UnknownBody> bodies;

    public BackupStepHandler(String name, double interval, ArrayList<UnknownBody> bodies) {
        this.interval = interval;
        this.name = name;
        this.bodies = bodies;
    }

    public void init(double t0, double[] y0, double t) {
        this.t0 = t0;
        this.lastT = t0;
    }

    public void handleStep(StepInterpolator interpolator, boolean isLast) {
        double   t = interpolator.getCurrentTime();
        double[] y = interpolator.getInterpolatedState();

        if(t > lastT+interval) {

            HandleIVs IVsaver = new HandleIVs();
            IVsaver.saveIVs(name,new IVs(bodies,y).getList());

            lastT = t;
        }

    }

}