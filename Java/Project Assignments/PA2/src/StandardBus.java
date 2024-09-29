/**
 * This class extends Bus and implements RefundProtocol, represents a bus with specific seat size and functionality to
 * sell tickets, managing seat allocation, revenue, and refunds for a standard bus service.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public class StandardBus extends Bus implements RefundProtocol {
    private int seatSize;
    private double refundCut, refundRevenue;
    private String[] busPlan;

    // Constructor.
    public StandardBus(String type, int id, String from, String to, int rows, double price, double refundCut){
        super(type, from, to, id, rows, price);
        this.refundCut = refundCut;
        this.refundRevenue = 0;
        this.setSeatSize(); // Initialize the seatSize.
        this.busPlan = new String[this.seatSize]; // Size same as the seatSize.
        for (int i = 0; i < this.busPlan.length; i++){
            this.busPlan[i] = "*"; // All seats declared empty at first.
        }
    }

    public double getRefundRevenue(){
        return this.refundRevenue;
    }
    public void setRefundRevenue(double refund){ // This will add the parameter to the refundRevenue.
        this.refundRevenue += refund;
    }
    public int getSeatSize(){
        return this.seatSize;
    }
    public void setSeatSize(){ // Use the number of seat in a single row to calculate seatSize.
        this.seatSize = this.getRows() * 4;
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
        if (this.busPlan[seatNumberIndex] == "*"){
            this.busPlan[seatNumberIndex] = "X";
            this.setRevenue(this.getPrice());
        }
        return isProcessed;
    } 
    
     /**
     * This method calculates and processes the refund amount for a given seat number in
     * a bus plan.
     * 
     * @param seatNumber    Represents the seat number of the ticket that is being sold.
     * @return  double   Represents the refund amount for a specific seat number.
     */
    public double refund(int seatNumber) {
        int seatNumberIndex = seatNumber - 1;
        this.busPlan[seatNumberIndex] = "*";
        double refundAmount = (this.getPrice()*(100-refundCut))/100; // Calculate refundAmount by cutting refundCut.
        this.setRevenue(-refundAmount); // Decrease the revenue by the refundAmount.
        this.setRefundRevenue(this.getPrice() - refundAmount); // Increase the refundRevenue by what's left after calculating refundCut.
        return refundAmount;
    }
}
