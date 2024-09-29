import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class represents a drill object in the game. The drill can move around the map, collect mines, and use fuel.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public class Drill {
    private int x, y, haul, money;
    private double fuel;
    // The images of the drill according to directions.
    private final Image left = new Image("assets/drill/drill_01.png");
    private final Image right = new Image("assets/drill/drill_56.png");
    private final Image up = new Image("assets/drill/drill_23.png"); 
    private final Image down = new Image("assets/drill/drill_43.png");
    private ImageView drillView;
    
    // Creates a new Drill object with initial position, fuel, haul, and money.
    public Drill(){
        this.haul = 0;
        this.money = 0;
        this.x = 430;
        this.y = 50;
        this.fuel = 10000;
        this.drillView = new ImageView(right);
        drillView.setX(x);
        drillView.setY(y);
    }

    // Setters and getters.
    public double getFuel() {
        return this.fuel;
    }
    public void setFuel(double fuel) {
        this.fuel = fuel; 
    }
    public int getHaul() {
        return this.haul;
    }
    public void setHaul(int haul) {
        this.haul = haul; 
    }
    public int getMoney() {
        return this.money;
    }
    public void setMoney(int money) {
        this.money = money; 
    }
    public int getX() {
        return this.x;
    }
    // Drill cannot move out of bounds.
    public void setX(int x) {
        if (x < 818){
            this.x = x;
            this.drillView.setX(x);
        }   
    }
    public int getY() {
        return this.y;
    }
    // Drill cannot move out of bounds.
    public void setY(int y) {
        if (y < 797){
            this.y = y;
            this.drillView.setY(y);
        }  
    }
    public Image getLeft() {
        return this.left;
    }
    public Image getRight() {
        return this.right;
    }

    public Image getUp() {
        return this.up;
    }
    public Image getDown() {
        return this.down;
    }
    public ImageView getDrillView() {
        return this.drillView;
    }

    /**
    * Moves the drill right by 50 pixels if it's within the map boundaries and updates the image to face right.
    */
    public void goRight(){
        if (this.x + 50 < 768 ) {
            this.setX(this.x + 50);
            this.turnRight();
        }
    }
    /**
    * Moves the drill left by 50 pixels if it's within the map boundaries and updates the image to face left.
    */
    public void goLeft(){
        if (-50 <= this.x - 50){
            this.setX(this.x - 50);
            this.turnLeft();
        }
    }
    /**
    * Moves the drill up by 50 pixels if it's within the map boundaries and updates the image to face up.
    */
    public void goUp(){
        if (0 <= this.y - 50) {
            this.setY(this.y - 50);
            this.turnUp();
        }
    }
    /**
    * Moves the drill down by 50 pixels if it's within the map boundaries and updates the image to face down.
    */
    public void goDown(){
        if (this.y + 50 < 747) {
            this.setY(this.y + 50);
            this.turnDown();
        }
    }

    /**
    * Updates the drill's image to face right.
    */
    public void turnRight(){
        this.drillView.setImage(right);
    }
    /**
    * Updates the drill's image to face left.
    */
    public void turnLeft(){
        this.drillView.setImage(left);
    }
    /**
    * Updates the drill's image to face up.
    */
    public void turnUp(){
        this.drillView.setImage(up);
    }
    /**
    * Updates the drill's image to face down.
    */
    public void turnDown(){
        this.drillView.setImage(down);
    }
}
