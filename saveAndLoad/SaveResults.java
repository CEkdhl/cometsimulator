package cometsim.saveAndLoad;

import cometsim.KnownBody;
import cometsim.Simulator;
import cometsim.UnknownBody;
import org.apache.commons.math3.ode.ContinuousOutputModel;

import java.io.*;
import java.util.ArrayList;

/**
 * Saves a simulator object with its data in a serialized file, or just the result of the simulation as a text file.
 */
public class SaveResults {

    Simulator sim;
    String name;

    /**
     * Basic empty constructor.
     */
    public SaveResults() {

    }

    /**
     * Basic constructor.
     * @param name Base name of files created using this instance. (String)
     * @param sim Simulator object (Simulator)
     */
    public SaveResults(String name, Simulator sim) {
        this.sim = sim;
        this.name = name;
    }

    /**
     * Like saveSerialized but using the object and name given to the constructor.
     */
    public void saveSerialized() {
        saveSerialized(name,sim);
    }

    /**
     * Like saveText but using the object and name given to the constructor.
     */
    public void saveText() {
        saveText(name, sim);
    }

    /**
     * Like saveMetaText but using the object and name given to the constructor.
     */
    public void saveMetaText() {
        saveMetaText(name + "_meta", sim);
    }

    /**
     * Like saveTextKnownObjects but using the object and name givent to the constructor.
     */
    public void saveTextKnownObjects() {
        saveTextKnownObjects(name+"_known", sim);
    }

    /**
     * Saves the simulator object in serialized form to a file.
     * @param name name of the filer, excluding the file extension (string)
     * @param sim simulator object (Simulator)
     */
    public void saveSerialized(String name, Simulator sim) {
        try{
            FileOutputStream f = new FileOutputStream(name+".ser");
            ObjectOutputStream out = new ObjectOutputStream(f);
            out.writeObject(sim);
            out.close();
            f.close();
        } catch( IOException i ) {
            i.printStackTrace();
            System.out.println("Could not create file.");
        }
    }

    /**
     * Writes the simulated results to a text file.
     * @param name name of the file excluding the file extension (string)
     * @param sim simulator object (Simulator)
     */
    public void saveText(String name, Simulator sim) {
        try {
            PrintWriter writer = new PrintWriter(new File(name+".txt"),"UTF-8");

            ContinuousOutputModel stepHandler = sim.getStepHandler();
            for(int i=(int)Math.ceil(sim.getT0()); i<(int)Math.ceil(sim.getTf()); i++) {
                stepHandler.setInterpolatedTime(i);
                String line = i+" ";
                for( double val: stepHandler.getInterpolatedState() ) {
                    line+= val+" ";
                }
                writer.println(line);
            }

            writer.close();
        } catch( IOException i ) {
            System.out.println("Could not create file.");
        }
    }

    /**
     * Save meta data about a given simulation.
     * @param name name of the file excluding the file extension (string)
     * @param sim simulatoor object (Simulator)
     */
    public void saveMetaText(String name, Simulator sim) {
        try {
            PrintWriter writer = new PrintWriter(new File(name+".txt"),"UTF-8");

            writer.println("T0 "+sim.getT0());
            writer.println("Tf "+sim.getTf());
            writer.println("AbsTol "+sim.getAbsoluteTolerance());
            writer.println("RelTol "+sim.getRelativeTolerance());
            writer.println("MaxStep "+sim.getMaxStep());
            writer.println("MinStep "+sim.getMinStep());
            writer.println("Timing "+sim.getTiming());

            String labels = "";
            for(UnknownBody b: sim.getUnknownBodies()) {
                labels += b.getID()+",";
            }

            writer.println("Labels "+labels);

            String knownBodies = "";
            for(KnownBody b: sim.getKnownBodies()) {
                knownBodies += b.getID()+",";
            }

            writer.println("KnownBodies " + knownBodies);

            writer.close();
        } catch( IOException i ) {
            System.out.println("Could not create file.");
        }
    }

    /**
     * Save the position of the planets at the specified times.
     * @param name Name of the file excluding extension.
     * @param sim Simulator object (Simulator)
     */
    public void saveTextKnownObjects(String name, Simulator sim) {
        ArrayList<KnownBody> knownBodies = sim.getKnownBodies();

        double[][][] coordinates = new double[knownBodies.size()][(int) sim.getTf()-(int) sim.getT0()][3];

        for(int i=0; i<knownBodies.size(); i++) {
            for(int t=(int)sim.getT0(); t<sim.getTf()-1; t++) {
                coordinates[i][t] = knownBodies.get(i).coordinates(t);
            }
        }

        coordinates = transpose(coordinates);

        try{
            PrintWriter writer = new PrintWriter(new File(name+".txt"),"UTF-8");

            for(int t=0; t<coordinates.length; t++) {
                String str = t + " ";
                for(int i=0; i<coordinates[0].length; i++) {
                    str += coordinates[t][i][0] + " " + coordinates[t][i][1] + " " + coordinates[t][i][2] + " ";
                }
                writer.println(str);
            }

            writer.close();
        } catch( IOException i ) {
            System.out.println("Could not create file.");
        }

    }

    public static double[][][] transpose(double [][][] m){
        double[][][] temp = new double[m[0].length][m.length][3];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

}