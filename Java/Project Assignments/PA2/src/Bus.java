/**
 * This class is an abstract class representing a bus with attributes such as type, origin,
 * destination, ID, rows, price, and revenue, along with abstract methods for seat size, selling
 * tickets, and getting the bus plan.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public abstract class Bus {
    private String busType, from, to;
    private int id, rows;
    private double price, revenue;

    // Constructor.
    public Bus(String type, String from, String to, int id, int rows, double price){
        this.busType = type;
        this.from = from;
        this.to = to;
        this.id = id;
        this.rows = rows;
        this.price = price;
        this.revenue = 0;
    }

    // Getters and setters.
    public String getBusType() {
        return this.busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    public String getFrom() {
        return this.from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return this.to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getRows() {
        return this.rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public double getRevenue() {
        return this.revenue;
    }
    public void setRevenue(double profit) {
        this.revenue += profit; // Add profit to total revenue
    }
    
    public abstract int getSeatSize(); // Different seat size getter and setter for different type buses.

    public abstract void setSeatSize();

    public abstract boolean sellTicket(int seatNumber); // Different sellTicket method for different type buses.

    public abstract String[] getBusPlan(); // all type buses should give information about their seat plan.

}

