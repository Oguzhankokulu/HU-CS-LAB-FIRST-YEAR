import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * This class represents a piece of earth (ground) in the game world. Each piece of earth can be of different types (grass, obstacle, etc.) 
 * and have different properties like value and weight.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public class Earth {
    private Image groundImage;
    private ImageView groundView;
    private int x, y, value, weight;
    private String name;
    
    /**
    * Creates a new Earth object with a specified name, position (x, y), and sets its image and properties based on the name.
    *
    * @param name The name of the earth type (e.g., "Grass", "Ironium").
    * @param x The x-coordinate of the earth piece.
    * @param y The y-coordinate of the earth piece.
    */
    public Earth (String name, int x, int y) {
        Random random = new Random(); // To use random methods.
        this.x = x;
        this.y = y;
        this.name = name;
        switch (name) { // Set the object and it's visuals according to given name.
            case "Grass":
            // Pick random grass asset.
            int grassNumber = random.nextInt(2);
            Image grass1 = new Image("assets/underground/top_01.png");
            Image grass2 = new Image("assets/underground/top_02.png");
            Image[] grassTypes = {grass1, grass2};
            this.groundImage = grassTypes[grassNumber]; // Random selection.
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            this.value = 0;
            this.weight = 0;
            break;
            case "Obstacle":
            // Pick random obstacle asset.
            Image obstacle1 = new Image("assets/underground/obstacle_01.png");
            Image obstacle2 = new Image("assets/underground/obstacle_02.png");
            Image obstacle3 = new Image("assets/underground/obstacle_03.png");
            Image[] obstacleTypes = {obstacle1, obstacle2, obstacle3};
            int obstacleNumber = random.nextInt(3);
            this.groundImage = obstacleTypes[obstacleNumber]; // Random selection.
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            this.value = 0;
            this.weight = 0;
            break;
            case "Soil":
            // Pick random soil asset.
            Image soil1 = new Image("assets/underground/soil_01.png");
            Image soil2 = new Image("assets/underground/soil_02.png");
            Image soil3 = new Image("assets/underground/soil_03.png");
            Image[] soilTypes = {soil1, soil2, soil3};
            int soilNumber = random.nextInt(3);
            this.groundImage = soilTypes[soilNumber]; // Random selection.
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            this.value = 0;
            this.weight = 0;
            break;
            case "Lava":
            // Pick random lava asset.
            Image lava1 = new Image("assets/underground/lava_01.png");
            Image lava2 = new Image("assets/underground/lava_02.png");
            Image lava3 = new Image("assets/underground/lava_03.png");
            Image[] lavaTypes = {lava1, lava2, lava3};
            int lavaNumber = random.nextInt(3);
            this.groundImage = lavaTypes[lavaNumber]; // Random selection.
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            break;
            case "Ironium":
            this.groundImage = new Image("assets/underground/valuable_ironium.png");
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            this.value = 30;
            this.weight = 10;
            break;
            case "Bronzium":
            this.groundImage = new Image("assets/underground/valuable_bronzium.png");
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            this.value = 60;
            this.weight = 10;
            break;
            case "Silverium":
            this.groundImage = new Image("assets/underground/valuable_silverium.png");
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            this.value = 100;
            this.weight = 10;
            break;
            case "Goldium":
            this.groundImage = new Image("assets/underground/valuable_goldium.png");
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            this.value = 250;
            this.weight = 20;
            break;
            case "Platinum":
            this.groundImage = new Image("assets/underground/valuable_platinum.png");
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            this.value = 750;
            this.weight = 30;
            break;
            case "Einsteinium":
            this.groundImage = new Image("assets/underground/valuable_einsteinium.png");
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            this.value = 2000;
            this.weight = 40;
            break;
            case "Emerald":
            this.groundImage = new Image("assets/underground/valuable_emerald.png");
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            this.value = 5000;
            this.weight = 60;
            break;
            case "Ruby":
            this.groundImage = new Image("assets/underground/valuable_ruby.png");
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            this.value = 20000;
            this.weight = 80;
            break;
            case "Diamond":
            this.groundImage = new Image("assets/underground/valuable_diamond.png");
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            this.value = 100000;
            this.weight = 100;
            break;
            case "Amazonite":
            this.groundImage = new Image("assets/underground/valuable_amazonite.png");
            this.groundView = new ImageView(this.groundImage);
            // Set coordinates of ImageView.
            groundView.setX(x);
            groundView.setY(y);
            this.value = 500000;
            this.weight = 120;
            break;
        }
    }

    // Setters and getters.
    public Image getGroundImage() {
        return this.groundImage;
    }
    public ImageView getGroundView() {
        return this.groundView;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public String getName() {
        return this.name;
    }
    public int getValue() {
        return this.value;
    }
    public int getWeight() {
        return this.weight;
    }

    /**
    * Removes the ImageView of this earth piece from the given Pane.
    *
    * @param root The Pane from which to remove the ImageView.
    */
    public void remover(Pane root){
        root.getChildren().remove(this.groundView);
    }
}
