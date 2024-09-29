import java.util.ArrayList;

/**
 * The code defines classes for building classrooms with specific dimensions and decorations.
 * It creates final version of a classroom by using Builder Design Pattern.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public class Builder {
    private String name, classType;
    private double width, length, height;
    private Decoration wall, floor;

    // Getters.
    public String getName() {
        return this.name;
    }
    public String getClassType(){
        return this.classType;
    }
    public double getWidth() {
        return this.width;
    }
    public double getLength() {
        return this.length;
    }
    public double getHeight() {
        return this.height;
    }
    public Decoration getWall() {
        return this.wall;
    }
    public Decoration getFloor() {
        return this.floor;
    }

    /**
     * The `itemBuild` method parses a line of input, creates either a `ClassroomBuilder` or
     * `Decoration` object based on the first part of the input, and adds it to the corresponding
     * ArrayList. In the `itemBuild` method, when the first element of the `parts` array is
     * not equal to "CLASSROOM", a new `Decoration` object is created using the data from the `parts
     * 
     * @param line  input line.
     * @param classrooms    An ArrayList of ClassroomBuilder objects.
     * @param decorations   The `decorations` parameter is an ArrayList that stores objects of the
     *                      `Decoration` class.
     */
    public static void itemBuild(String line, ArrayList<ClassroomBuilder> classrooms, ArrayList<Decoration> decorations){
        String[] parts = line.split("\t");
        if (parts[0].equals("CLASSROOM")) {
            classrooms.add(new ClassroomBuilder(parts));
        } else {
            decorations.add(new Decoration(parts));
        }
    }

    /**
     * This Java function takes a line of input, retrieves classroom and decoration information from
     * ArrayLists, creates a Builder object based on the input data, and returns a CircularClassroom or
     * RectangularClassroom object depending on the class type.
     * 
     * @param line  input line
     * @param classrooms    ArrayList of Classroom type items.
     * @param decorations   ArrayList of Decoration type items.
     * @return An instance of either `CircularClassroom` or `RectangularClassroom` is being returned
     * based on the class type specified in the input data.
     */
    public static Classroom built(String line, ArrayList<ClassroomBuilder> classrooms, ArrayList<Decoration> decorations){
        String[] parts = line.split("\t"); // Split the input. Input form example: "C1  D3  D4".
        String className = parts[0];
        String wallName = parts[1];
        String floorName = parts[2];
        Builder temp = new Builder(); // Temporary Builder class object.

        for (int i = 0; i < classrooms.size(); i++){ // For every classroom item.
            if (className.equals(classrooms.get(i).getName())) { // If the input class name is equal to the current class's name, set variables.
                temp.name = classrooms.get(i).getName();
                temp.classType = classrooms.get(i).getType();
                temp.width = classrooms.get(i).getWidth();
                temp.length = classrooms.get(i).getLength();
                temp.height = classrooms.get(i).getHeight();
            }

        }for (int j = 0; j < decorations.size(); j++){ // For every decoration item.
            if (wallName.equals(decorations.get(j).getName())){ // If the input decoration name for the wall is equal to the current decoration's name, assign decoration object to object variable.
                temp.wall = decorations.get(j);
            }
        }for (int k = 0; k < decorations.size(); k++){ // For every decoration item.
            if (floorName.equals(decorations.get(k).getName())){ // If the input decoration name for the floor is equal to the current decoration's name, assign decoration object to object variable.
                temp.floor = decorations.get(k);
            }
        }
        Builder temp2 = temp;
        temp = new Builder(); // Reset the temporary object.
        if ( temp2.classType.equals("Circle")){ // If class's type is circle.
            return new CircularClassroom(temp2);
        } else {
            return new RectangularClassroom(temp2);
        }
    }
}
/**
 * The `ClassroomBuilder` class in Java represents a builder for creating classroom objects with
 * properties such as name, type, width, length, and height.
 */
class ClassroomBuilder {
    private String className, classType;
    private double classWidth, classLength, classHeight;

    // Getters.
    public String getName() {
        return this.className;
    }
    public String getType() {
        return this.classType;
    }
    public double getWidth() {
        return this.classWidth;
    }
    public double getLength() {
        return this.classLength;
    }
    public double getHeight() {
        return this.classHeight;
    }

    // Constructor
    public ClassroomBuilder(String[] parts){
        this.className = parts[1];
        this.classType = parts[2];
        this.classWidth = Double.parseDouble(parts[3]);
        this.classLength = Double.parseDouble(parts[4]);
        this.classHeight = Double.parseDouble(parts[5]);
    }
}