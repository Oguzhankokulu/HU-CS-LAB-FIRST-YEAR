/*
 * This interface declares a single refund method. The point of creating
 * this interface is because taking advantage of Polymorphism and to be sure
 * implementing refund method. By using this interface, it's possible to run
 * refund method with different classes by using Polymorphism.
 */
public interface RefundProtocol {
    double refund(int seatNumber); // This method processes refund commands.
}
