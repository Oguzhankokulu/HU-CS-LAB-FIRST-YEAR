/**
 * This class represents a point in the map with a name attribute and provides methods to get and set the
 * name.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public class Point {
    private String name;

    // Constructor.
    public Point(String name){
        this.name = name;
    }

    // Setters and getters.
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Point findPoind(Connection connection, Point endPoint){
        Point point1 = connection.getPoint1();
        Point point2 = connection.getPoint2();

        if (point1.getName().equals(endPoint.getName())){
            return point2;
        } else {
            return point1;
        }
    }
}
