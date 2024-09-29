/**
 * The `Decoration` class represents a decoration object with properties such as name, type, price, and
 * tile area if it is of type "Tile".
 * 
 * @author Oguzhan
 * @version 1.0
 */
public class Decoration {
    private String decorName, decorType;
    private double priceDecor, tileArea;

    // Getters.
    public String getDecorType(){
        return this.decorType;
    }
    public double getPriceDecor() {
        return this.priceDecor;
    }
    public String getName() {
        return this.decorName;
    }
    public double getTileArea(){
        return this.tileArea;
    }

    // Constructor.
    public Decoration(String[] parts){
        this.decorName = parts[1];
        this.priceDecor = Double.parseDouble(parts[3]);
        this.decorType = parts[2];
        if (this.decorType.equals("Tile")){
            this.tileArea = Double.parseDouble(parts[4]);
        }
    } 
}