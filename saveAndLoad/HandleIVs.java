package cometsim.saveAndLoad;

import cometsim.UnknownBody;

import java.io.*;
import java.util.ArrayList;

public class HandleIVs {

    /**
     * Save information about state in a way that they may be loaded and the integration continued later.
     * @param name Name of file (String)
     * @param bodies
     */
    public void saveIVs(String name, ArrayList<UnknownBody> bodies) {
        try{
            FileOutputStream f = new FileOutputStream(name+".ser");
            ObjectOutputStream out = new ObjectOutputStream(f);
            out.writeObject(bodies);
            out.close();
            f.close();
        } catch( IOException i ) {
            i.printStackTrace();
            System.out.println("Could not create file.");
        }
    }

    /**
     * Load stored initial values.
     * @param name Name of file (String)
     * @return List of UnknownBody objects that can be added to a simulation
     */
    public ArrayList<UnknownBody> loadIVs(String name) {
        ArrayList<UnknownBody> bodies = new ArrayList<UnknownBody>();

        try{
            FileInputStream in1 = new FileInputStream(name+".ser");
            ObjectInputStream s1 = new ObjectInputStream(in1);
            bodies = (ArrayList<UnknownBody>) s1.readObject();
        } catch(Exception e) { System.out.println("Could not load file."); }

        return bodies;
    }

}