public class Stationery extends Product{
    private String kind;

    // Constructor
    public Stationery(String name, String kind, int barcode, double price){
        super(name, barcode, price);
        this.kind = kind;
    }

    public String getKind() {
        return this.kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
}
