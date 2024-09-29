/**
 * The `Classroom` class represents a classroom with dimensions and decorations, and can calculate the
 * price based on a given path.
 * It holds information about the name, widht, lenght, height, wall, floor.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public abstract class Classroom{
    private String name;
    private double width, length, height;
    private Decoration wall, floor;


    public Classroom(Builder builder){ // Create Classroom object by using builder class.
        this.name = builder.getName();
        this.width = builder.getWidth();
        this.length = builder.getLength();
        this.height = builder.getHeight();
        this.wall = builder.getWall();
        this.floor = builder.getFloor();
    }

    // Getters.
    public String getName() {
        return this.name;
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
     * This Java function is an abstract method that calculates the price based on a given path.
     * 
     * @param path  The `path` parameter in the `calculatePrice` method is a string that represents the
     *              file path or location of a resource.
     * @return double   Returns totalPrice. 
     */
    public abstract double calculatePrice(String path);
}

/**
 * This class is a subclass of abstract class Classroom.
 * It represents a circular shaped classroom.
 * It implements calculatePrice() method for its objects by using its super variables.
 * 
 * @author Oguzhan
 * @version 1.0
 */
class CircularClassroom extends Classroom{
    public CircularClassroom(Builder builder){
        super(builder);
    }

    /**
     * This Java function calculates the total price for decorating a circular classroom's walls and floor based
     * on the area and type of decoration used.
     * 
     * @param path  The `path` parameter in the `calculatePrice` method is the file path where the
     *              output will be written.
     * @return double   The method `calculatePrice` is returning the total price of decorating a classroom,
     *                  which is calculated based on the wall and floor decorations used, their areas, and prices.
     */
    @Override
    public double calculatePrice(String path){
        Decoration decorWall = this.getWall();
        Decoration decorFloor = this.getFloor();
        double wallArea = 2 * ((this.getWidth())/2) * this.getHeight() * Math.PI;
        double floorArea = Math.ceil(Math.PI * ((this.getWidth())/2) * ((this.getWidth())/2));
        double wallPrice = 0;
        double floorPrice = 0;
        int totalPrice = 0;
        int tileWallNumber = 0;
        String formatStr = "";

        if (decorWall.getDecorType().equals("Tile")) { // If the decor ,which will be placed in the wall, is Tile.
            tileWallNumber = (int) Math.ceil((wallArea / decorWall.getTileArea())); // Calculate Tile number.
            wallPrice = tileWallNumber * decorWall.getPriceDecor(); // Calculate price for wall decoration.

        } else {
            wallPrice = wallArea * decorWall.getPriceDecor(); // Calculate price for wall decoration by considering price per m2.
        }
        int tileFloorNumber = (int) Math.ceil((floorArea / decorFloor.getTileArea())); // Calculate Tile number for floor.
        floorPrice = tileFloorNumber * decorFloor.getPriceDecor(); // Calculate price for wall decoration by considering price per tile.
        totalPrice = (int) Math.ceil(wallPrice + floorPrice); // Calculate and round  up total price for walls and floor.

        if (decorWall.getDecorType().equals("Tile")){ // If the decor ,which will be placed in the wall, is Tile.
            formatStr = String.format("Classroom %s used %d Tiles for walls and used %d Tiles for flooring, these costed %dTL.", this.getName(), tileWallNumber, tileFloorNumber, totalPrice );
            FileOutput.writeToFile(path, formatStr, true, true);
        } else {
            formatStr = String.format("Classroom %s used %dm2 of %s for walls and used %d Tiles for flooring, these costed %dTL.", this.getName(), (int) Math.ceil(wallArea), decorWall.getDecorType(), tileFloorNumber, totalPrice);
            FileOutput.writeToFile(path, formatStr, true, true);
        }
        return Math.ceil(totalPrice);
    }
}

/**
 * This class is a subclass of abstract class Classroom.
 * It represents a rectangular shaped classroom.
 * It implements calculatePrice() method for its objects by using its super variables.
 * 
 * @author Oguzhan
 * @version 1.0
 */
class RectangularClassroom extends Classroom{
    public RectangularClassroom(Builder builder){
        super(builder);
    }

    /**
     * This Java function calculates the total price for decorating a rectangular classroom's walls and floor based
     * on the area and type of decoration used.
     * 
     * @param path  The `path` parameter in the `calculatePrice` method is the file path where the
     *              output will be written.
     * @return double   The method `calculatePrice` is returning the total price of decorating a classroom,
     *                  which is calculated based on the wall and floor decorations used, their areas, and prices.
     */
    @Override
    public double calculatePrice(String path){
        Decoration decorWall = this.getWall();
        Decoration decorFloor = this.getFloor();
        double wallArea = (2 * this.getLength() * this.getHeight()) + (2 * this.getWidth() * this.getHeight());
        double floorArea = this.getLength() * this.getWidth();
        double wallPrice = 0;
        double floorPrice = 0;
        int totalPrice = 0;
        int tileWallNumber = 0;
        String formatStr = "";

        if (decorWall.getDecorType().equals("Tile")) { // If the decor ,which will be placed in the wall, is Tile.
            tileWallNumber = (int) Math.ceil((wallArea / decorWall.getTileArea())); // Calculate Tile number.
            wallPrice = tileWallNumber * decorWall.getPriceDecor(); // Calculate price for wall decoration by considering price per tile.

        } else {
            wallPrice = wallArea * decorWall.getPriceDecor(); // Calculate price for wall decoration by considering price per m2.
        }
        int tileFloorNumber = (int) Math.ceil((floorArea / decorFloor.getTileArea())); // Calculate Tile number for floor.
        floorPrice = tileFloorNumber * decorFloor.getPriceDecor();  // Calculate price for wall decoration by considering price per tile.
        totalPrice = (int) Math.ceil(wallPrice + floorPrice); // Calculate and round  up total price for walls and floor.

        if (decorWall.getDecorType().equals("Tile")){ // If the decor ,which will be placed in the wall, is Tile.
            formatStr = String.format("Classroom %s used %d Tiles for walls and used %d Tiles for flooring, these costed %dTL.", this.getName(), tileWallNumber, tileFloorNumber, totalPrice );
            FileOutput.writeToFile(path, formatStr, true, true);
        } else {
            formatStr = String.format("Classroom %s used %dm2 of %s for walls and used %d Tiles for flooring, these costed %dTL.", this.getName(), (int) Math.ceil(wallArea), decorWall.getDecorType(), tileFloorNumber, totalPrice);
            FileOutput.writeToFile(path, formatStr, true, true);
        }
        return Math.ceil(totalPrice);

    }
}
