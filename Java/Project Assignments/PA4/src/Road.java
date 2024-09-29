import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents a road with length and ID, and provides a method to sort a list
 * of `Connection` objects based on road length and ID.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public class Road {
    private int length, id;

    // Constructor.
    public Road(int length, int id){
        this.length = length;
        this.id = id;
    }

    // Setters and getters.
    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    /**
     * this method sorts an ArrayList of `Connection` objects first by road length and
     * then by road ID.
     * 
     * @param arraylist An arraylist of Connection objects which will be sorted.
     */
    public static void sortRoads(ArrayList<Connection> arraylist){
        // Lambda expression to sort according to road length and if equal then road id.
        Collections.sort(arraylist, (c1, c2) -> {
            int distanceComparision = c1.getRoadLength() - c2.getRoadLength();
            if (distanceComparision != 0) {
                return distanceComparision;
            } else {
                return c1.getRoadId() - c2.getRoadId();
            }
        });
    }
}
