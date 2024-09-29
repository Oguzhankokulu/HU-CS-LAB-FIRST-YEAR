public class Toy extends Product{
    private String color;

    // Constructor
    public Toy(String name, String color, int barcode, double price){
        super(name, barcode, price);
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }
    public void setColor(String color) {
        this.color = color;
    }
}
