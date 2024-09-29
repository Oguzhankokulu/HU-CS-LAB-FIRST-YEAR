import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class represents the main application for the HU-Load game.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public class Main extends Application {

    /**
     * The main method to launch the application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
            launch(args);
        }
       
     /**
     * Overrides the start method of Application class.
     * @param primaryStage The primary stage of the application.
     * @throws Exception Possible exception during application start.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Arraylist to store underground blocks.
        ArrayList<Earth> ground = new ArrayList<Earth>();
        Drill drill = new Drill();

        Stage gameWindow = new Stage();
        Pane root = new Pane();
        // New scene to set sky.
        Scene scene = new Scene(root, Color.DARKBLUE);
        // Request focus to root to be able to move the drill with arrow keys.
        root.requestFocus();


        Rectangle underground = new Rectangle(); // Background of underground.
        underground.setWidth(800); // Set width in pixels
        underground.setHeight(650); // Set height in pixels
        underground.setFill(javafx.scene.paint.Color.color(0.55, 0.25, 0)); // Brown color.
        underground.setLayoutY(103);
        root.getChildren().add(underground);

        // Fuel text in the top left corner.
        Text fuelText = new Text("fuel:" + drill.getFuel());
        fuelText.setX(5);
        fuelText.setY(30);
        fuelText.setFont(Font.font(20));
        fuelText.setFill(Color.WHITE);
        // Haul text in the top left corner.
        Text haulText = new Text("haul:" + drill.getHaul());
        haulText.setX(5);
        haulText.setY(60);
        haulText.setFont(Font.font(20));
        haulText.setFill(Color.WHITE);
        // Money text in the top left corner.
        Text moneyText = new Text("money:" + drill.getMoney());
        moneyText.setX(5);
        moneyText.setY(90);
        moneyText.setFont(Font.font(20));
        moneyText.setFill(Color.WHITE);
         
        root.getChildren().add(fuelText);
        root.getChildren().add(haulText);
        root.getChildren().add(moneyText); 


        for (int i = 0; i < 16 ; i++) { // Random grass placer.
            Earth grass = new Earth("Grass", i*50, 100); // Create the Earth object for grass.
            ground.add(grass); // Add blocks to ground arraylist
            root.getChildren().add(grass.getGroundView());
        }


        for (int row = 0; row < 24 ; row++) { // Random obstacle placer.
            if (row < 12) { // For left side.
                Earth obstacle = new Earth("Obstacle", 0, 150+row*50); // Create the Earth object for obstacle.
                ground.add(obstacle); // Add blocks to ground arraylist
                root.getChildren().add(obstacle.getGroundView());
            } else { // for right side.
                Earth obstacle = new Earth("Obstacle", 750, 150+(row-12)*50); // Create the Earth object for obstacle.
                ground.add(obstacle); // Add blocks to ground arraylist
                root.getChildren().add(obstacle.getGroundView());
            }
        }
        for (int column = 0; column < 14 ; column++) { // For bottom.
            Earth obstacle = new Earth("Obstacle", 50+50*column, 700); // Create the Earth object for obstacle.
            ground.add(obstacle); // Add blocks to ground arraylist
            root.getChildren().add(obstacle.getGroundView());
        }

        // Define mining rates for different types of earth blocks. Values represents the weighted possibilities.
        Map<String, Integer> mineRates = new HashMap<>();
        mineRates.put("Soil", 5000);
        mineRates.put("Lava", 175);
        mineRates.put("Ironium", 125);
        mineRates.put("Bronzium", 100);
        mineRates.put("Silverium", 80);
        mineRates.put("Goldium", 60);
        mineRates.put("Obstacle", 50);
        mineRates.put("Platinum", 50);
        mineRates.put("Einsteinium", 40);
        mineRates.put("Emerald", 30);
        mineRates.put("Ruby", 20);
        mineRates.put("Diamond", 15);
        mineRates.put("Amazonite", 10);
        int totalWeight = 5755;
        for (int row = 0; row < 11 ; row++){ // To fill underground.
            for (int column = 0; column < 14 ; column++) {
                int randomWeight = (int) (Math.random() * totalWeight); // Calculate random weight by using Math.random()
                int currentWeight = 0;
                boolean isPlaced = false;
                while (!isPlaced){ // While there is no block placed.
                    for (Map.Entry<String, Integer> entry : mineRates.entrySet()) { // For every mine in mineRates map.
                        // Current mine's value will be added respectively and when the current weight is greater or equal to the random
                        // weight it will be placed. This way, mines with greater values will be placed more. Weighted random selection logic is applied.
                        currentWeight += entry.getValue();
                        if (randomWeight <= currentWeight) {
                            Earth mine = new Earth(entry.getKey(), 50+50*column, 150+row*50);
                            ground.add(mine);
                            root.getChildren().add(mine.getGroundView());
                            isPlaced = true;
                            break;
                        }
                    }
                }
            }
        }

          // Add drill view to the scene.
        root.getChildren().add(drill.getDrillView());
        // Key event handler for drill movement.
        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP) {
                    drill.setFuel(drill.getFuel() - 100); // Decrease the fuel.
                    // Set coordinates of the drill.
                    int drillX = drill.getX() + 20;
                    int drillY = drill.getY() - 50;
                    boolean isBlocked = false;
                    for (Earth block : ground) {
                        int blockX = block.getX();
                        int blockY = block.getY();
                        if (drillX == blockX && drillY == blockY){ // If there is a block above.
                            isBlocked = true;
                            break;
                        }
                    }
                    drill.turnUp(); // Turn the drill up.
                    if (!isBlocked){ // If the above of the drill is not blocked, go up.
                        drill.goUp();
                    }
                } else if (event.getCode() == KeyCode.DOWN) {
                    drill.setFuel(drill.getFuel() - 100); // Decrease the fuel.
                    // Set coordinates of the drill.
                    int drillX = drill.getX() + 20;
                    int drillY = drill.getY() + 50;
                    boolean isBlocked = false;
                    for (Earth block : ground) {
                        int blockX = block.getX();
                        int blockY = block.getY();
                        if (block.getName().equals("Obstacle")){ // If obstacle come across.
                            if (drillX == blockX && drillY == blockY){
                                isBlocked = true;
                                break;
                            }
                        }
                        if (block.getName().equals("Lava")){ // If lava come across.
                            if (drillX == blockX && drillY == blockY){
                                // Make the red game over screen.
                                Pane gameOverPane = new Pane();
                                Scene gameOverScene = new Scene(gameOverPane, Color.RED);
                                Text gameOverText = new Text("GAME OVER");
                                gameOverPane.getChildren().add(gameOverText);
                                gameOverText.setX(250);
                                gameOverText.setY(350);
                                gameOverText.setFont(Font.font(50));
                                gameOverText.setFill(Color.WHITE);
                                gameWindow.setScene(gameOverScene);
                                break;
                            }
                        }
                        if (drillX == blockX && drillY == blockY){ // If the object is mineable, remove the object and get value and weight.
                            drill.setHaul(drill.getHaul() + block.getWeight());
                            drill.setMoney(drill.getMoney() + block.getValue());
                            block.remover(root);
                            ground.remove(block);
                            break;
                        }
                    }
                    // Update haul and money informations.
                    String haulFormat = String.format("haul:%d", drill.getHaul());
                    String moneyFormat = String.format("money:%d", drill.getMoney());
                    moneyText.setText(moneyFormat);
                    haulText.setText(haulFormat);
                    drill.turnDown(); // Turn the drill down.
                    if (!isBlocked){ // If the path isn't blocked go down.
                        drill.goDown();
                    }
                } else if (event.getCode() == KeyCode.LEFT) {
                    drill.setFuel(drill.getFuel() - 100); // Decrease the fuel.
                    // Set coordinates of the drill.
                    int drillX = drill.getX() - 30;
                    int drillY = drill.getY();
                    boolean isBlocked = false;
                    for (Earth block : ground) {
                        int blockX = block.getX();
                        int blockY = block.getY();
                        if (block.getName().equals("Obstacle")){ // If obstacle come across.
                            if (drillX == blockX && drillY == blockY){
                                isBlocked = true;
                                break;
                            }
                        }
                        if (block.getName().equals("Lava")){ // If lava come across.
                            if (drillX == blockX && drillY == blockY){
                                // Make the red game over screen.
                                Pane gameOverPane = new Pane();
                                Scene gameOverScene = new Scene(gameOverPane, Color.RED);
                                Text gameOverText = new Text("GAME OVER");
                                gameOverPane.getChildren().add(gameOverText);
                                gameOverText.setX(250);
                                gameOverText.setY(350);
                                gameOverText.setFont(Font.font(50));
                                gameOverText.setFill(Color.WHITE);
                                gameWindow.setScene(gameOverScene);
                                break;
                            }
                        }
                        if (drillX == blockX && drillY == blockY){ // If the object is mineable, remove the object and get value and weight.
                            drill.setHaul(drill.getHaul() + block.getWeight());
                            drill.setMoney(drill.getMoney() + block.getValue());
                            block.remover(root);
                            ground.remove(block);
                            break;
                        }
                    }
                    // Update haul and money informations.
                    String haulFormat = String.format("haul:%d", drill.getHaul());
                    String moneyFormat = String.format("money:%d", drill.getMoney());
                    moneyText.setText(moneyFormat);
                    haulText.setText(haulFormat);
                    drill.turnLeft(); // Turn the drill left.
                    if (!isBlocked){ // If the path isn't blocked go left.
                        drill.goLeft();
                    }
                } else if (event.getCode() == KeyCode.RIGHT) {
                    drill.setFuel(drill.getFuel() - 100); // Decrease the fuel.
                    // Set coordinates of the drill.
                    int drillX = drill.getX() + 70;
                    int drillY = drill.getY();
                    boolean isBlocked = false;
                    for (Earth block : ground) {
                        int blockX = block.getX();
                            int blockY = block.getY();
                        if (block.getName().equals("Obstacle")){ // If obstacle come across.
                            if (drillX == blockX && drillY == blockY){
                                isBlocked = true;
                                break;
                            }
                        }
                        if (block.getName().equals("Lava")){ // If lava come across.
                            if (drillX == blockX && drillY == blockY){
                                // Make the red game over screen.
                                Pane gameOverPane = new Pane();
                                Scene gameOverScene = new Scene(gameOverPane, Color.RED);
                                Text gameOverText = new Text("GAME OVER");
                                gameOverPane.getChildren().add(gameOverText);
                                gameOverText.setX(250);
                                gameOverText.setY(350);
                                gameOverText.setFont(Font.font(50));
                                gameOverText.setFill(Color.WHITE);
                                gameWindow.setScene(gameOverScene);
                                break;
                            }
                        }
                        if (drillX == blockX && drillY == blockY){ // If the object is mineable, remove the object and get value and weight.
                            drill.setHaul(drill.getHaul() + block.getWeight());
                            drill.setMoney(drill.getMoney() + block.getValue());
                            block.remover(root);
                            ground.remove(block);
                            break;
                        }
                    }
                    // Update haul and money informations.
                    String haulFormat = String.format("haul:%d", drill.getHaul());
                    String moneyFormat = String.format("money:%d", drill.getMoney());
                    moneyText.setText(moneyFormat);
                    haulText.setText(haulFormat);
                    drill.turnRight(); // Turn the drill right.
                    if (!isBlocked){ // If the path isn't blocked go right.
                        drill.goRight();
                    }
                }
            }
        });
 
        // Timeline for implementing gravity effect on drill.
        Timeline gravityLoop = new Timeline(new KeyFrame(Duration.seconds(0.75), event -> {
            boolean isGround = false;
            for (Earth block : ground){ // Check the blocks and see if there is a block under the drill.
                int blockX = block.getX();
                int blockY = block.getY();
                if (drill.getX() + 20 == blockX && drill.getY() + 50 == blockY){
                    isGround = true;
                    break;
                }
            }  
            if (!isGround){
                drill.setY(drill.getY() + 50);
            }
        }));
        gravityLoop.setCycleCount(Animation.INDEFINITE); // Constantly apply gravity.
        gravityLoop.play();

        
        // Timeline for updating fuel level with time.
        Timeline drillLoop = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            drill.setFuel(drill.getFuel()-0.010); // Decrease the fuel.
            String formattedStr = String.format("fuel:%.3f", drill.getFuel()); // Update the fuel text in the top left corner of the screen.
            fuelText.setText(formattedStr);
            if (drill.getFuel() < 0){ // If fuel is consumed then convert screen to the green game over screen.
                Pane gameOverPane = new Pane();
                Scene gameOverScene = new Scene(gameOverPane, Color.DARKGREEN);
                String formattedMoney = String.format("\t GAME OVER\nCollected Money: %d", drill.getMoney());
                Text gameOverText = new Text(formattedMoney);
                gameOverPane.getChildren().add(gameOverText);
                gameOverText.setX(130);
                gameOverText.setY(350);
                gameOverText.setFont(Font.font(50));
                gameOverText.setFill(Color.WHITE);
                gameWindow.setScene(gameOverScene);
            }
        }));
        drillLoop.setCycleCount(Animation.INDEFINITE); // Constantly apply fuel consumption.
        drillLoop.play();

        // Set the stage attributes.
        gameWindow.setTitle("HU-Load");
        // Set widht and height to nicely fit the blocks.
        gameWindow.setWidth(818);
        gameWindow.setHeight(797);
        // Make the game window not resizable.
        gameWindow.setResizable(false);
        gameWindow.setScene(scene);

        gameWindow.show();

    }
}
