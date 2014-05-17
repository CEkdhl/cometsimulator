package cometsim;

import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import java.io.File;
import java.io.PrintWriter;

public class WriteFileStepHandler implements StepHandler {

    double interval;
    double t0;
    String name;
    PrintWriter writer;
    double lastT;

    public WriteFileStepHandler(String name, double interval) {
        this.interval = interval;
        this.name = name;

        try{
            this.writer = new PrintWriter(new File(name+".txt"),"UTF-8");
        } catch(Exception e) { e.printStackTrace(); }
    }

    public void init(double t0, double[] y0, double t) {
        this.t0 = t0;
        this.lastT = t0;

        String str = "";
        for(int i=0; i<y0.length; i++) {
            str += y0[i] + " ";
        }

        writer.println(t0 + " " + str);
    }

    public void handleStep(StepInterpolator interpolator, boolean isLast) {
        double   t = interpolator.getCurrentTime();
        double[] y = interpolator.getInterpolatedState();

        if(t > lastT+interval) {
            String str = "";
            for(int i=0; i<y.length; i++) {
                str += y[i] + " ";
            }

            writer.println(t + " " + str);
            lastT = t;
        }

    }

}
