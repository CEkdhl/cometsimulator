package cometsim.saveAndLoad;

import cometsim.Body;
import cometsim.UnknownBody;
import cometsim.XYZCoordinates;

import java.util.ArrayList;

public class IVs {

    ArrayList<UnknownBody> bodies = new ArrayList<UnknownBody>();

    public IVs(ArrayList<UnknownBody> givenBodies,double[] values) {

        for(int i=0; i<values.length; i+=6) {
            XYZCoordinates elements = new XYZCoordinates(values[i], values[i+1], values[i+2], values[i+3], values[i+4], values[i+5]);

            try {
                Body newBody = givenBodies.get(i/6).clone();
                newBody.setElements(elements);
                bodies.add((UnknownBody) newBody);
            } catch(Exception e) { e.printStackTrace(); }
        }

    }

    public ArrayList<UnknownBody> getList() {
        return bodies;
    }

}
