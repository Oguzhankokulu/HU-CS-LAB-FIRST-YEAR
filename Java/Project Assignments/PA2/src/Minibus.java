/**
 * The Minibus class extends Bus and represents a minibus with specific seat size and functionality to
 * sell tickets.
 * 
 * @autor Oguzhan
 * @version 1.0
 */
public class Minibus extends Bus {
    private int seatSize;
    private String[] busPlan;

    // Constructor.
    public Minibus(String type, int id, String from, String to, int rows, double price){
        super(type, from, to, id, rows, price);
        this.setSeatSize(); // Initialize the seatSize.
        this.busPlan = new String[this.seatSize]; // Size same as the seatSize.
        for (int i = 0; i < this.busPlan.length; i++){
            this.busPlan[i] = "*"; // All seats declared empty at first.
        }
    }

    public int getSeatSize(){
        return this.seatSize;
    }
    public void setSeatSize(){ // Use the number of seat in a single row to calculate seatSize.
        this.seatSize = this.getRows() * 2;
    }
    public String[] getBusPlan(){
        return this.busPlan;
    }

    /**
     * This method sells a ticket for a specific seat number if the seat is available.
     * 
     * @param seatNumber    Represents the seat number of the ticket that is being sold.
     * @return  boolean  Returns a boolean value indicating whether the ticket sale was processed successfully.
     */
    public boolean sellTicket(int seatNumber){
        boolean isProcessed = false; 
        int seatNumberIndex = seatNumber - 1;
        if (this.busPlan[seatNumberIndex] == "*") { // If the seat is empty.
            this.busPlan[seatNumberIndex] = "X";
            isProcessed = true;
            this.setRevenue(this.getPrice()); // Increase the revenue by price.
        }
        
        return isProcessed;
    }   
}
