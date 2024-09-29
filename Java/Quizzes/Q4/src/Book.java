public class Book extends Product{
    private String author;

    // Constructor
    public Book(String name, String author, int barcode, double price){
        super(name, barcode, price);
        this.author = author;
    }

    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
     
}
