package cometsim.saveAndLoad;

import cometsim.Result;
import cometsim.Simulator;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The primary purpose of this class is to turn data (Simulator objects) into result (Result objects).
 * It can retrieve data from files with serialized objects. It can also combine several files or
 * several simulator objects into one list of result objects. As well as filter out unwanted objects.
 */
public class LoadResults {

    ArrayList<Result> results = new ArrayList<Result>();
    double t0;
    double tf;

    /**
     * Basic constructor. Sets up a list of result objects using the given simulator object.
     * @param sim Simulator object with the results.
     */
    public LoadResults(Simulator sim) {
        if(sim.getContinousOutput()) {
            t0 = sim.getT0();
            tf = sim.getTf();
            for(int i=0; i<sim.getUnknownBodies().size(); i++) {
                results.add(new Result(sim, i));
            }
        }
        else System.out.println("Could not load results because the simulator does not provide continuous output.");
    }

    /**
     * Basic constructor. Sets up a list of result objects using the given filename.
     * @param filename Filename excluding extension of a file containing a serialized copy of a simulator object
     */
    public LoadResults(String filename) {
        try{
            FileInputStream in1 = new FileInputStream(filename+".ser");
            ObjectInputStream s1 = new ObjectInputStream(in1);
            Simulator sim = (Simulator) s1.readObject();

            t0 = sim.getT0();
            tf = sim.getTf();
            for(int i=0; i<sim.getUnknownBodies().size(); i++) {
                results.add(new Result(sim, i));
            }
        } catch(Exception e) { System.out.println("Could not load file."); }
    }

    /**
     * Appends the bodies in the simulator object to the already existing list of objects.
     * @param sim
     */
    public void append(Simulator sim) {
        for(int i=0; i<sim.getUnknownBodies().size(); i++) {
            results.add(new Result(sim, i));
        }
    }

    /**
     * Appends the bodies in the simulator object serialized in the file to the already existing list of objects.
     * @param filename filename excluding extension to a file with the serialized simulator object
     */
    public void append(String filename) {
        try{
            FileInputStream in1 = new FileInputStream(filename+".ser");
            ObjectInputStream s1 = new ObjectInputStream(in1);
            Simulator sim = (Simulator) s1.readObject();

            for(int i=0; i<sim.getUnknownBodies().size(); i++) {
                results.add(new Result(sim, i));
            }
        } catch(Exception e) { System.out.println("Could not load file."); }
    }

    /**
     * Get the list of result objects
     * @return List of results (ArrayList<Result>)
     */
    public ArrayList<Result> getResults() {
        return results;
    }

    /**
     * Remove result object for which the body's ID is given.
     * @param id Identifier (String)
     */
    public void remove(String id) {
        Iterator<Result> it = results.iterator();
        while(it.hasNext()) {
            Result body = it.next();
            if(body.getID() == id) {
                it.remove();
                break;
            }
        }
    }

    /**
     * Remove all result objects which do not appear in this list of IDs.
     * @param IDs Identifiers (ArrayList<String>)
     */
    public void intersection(ArrayList<String> IDs) {
        Iterator<Result> it = results.iterator();
        while(it.hasNext()) {
            Result body = it.next();
            if(!IDs.contains(body.getID())) {
                it.remove();
                break;
            }
        }
    }

    /**
     * Remove all result objects wich appear in this list of IDs.
     * @param IDs Identifiers (ArrayList<String>)
     */
    public void complement(ArrayList<String> IDs) {
        Iterator<Result> it = results.iterator();
        while(it.hasNext()) {
            Result body = it.next();
            if(IDs.contains(body.getID())) {
                it.remove();
                break;
            }
        }
    }

    public double getT0() {
        return t0;
    }

    public double getTf() {
        return tf;
    }
}
