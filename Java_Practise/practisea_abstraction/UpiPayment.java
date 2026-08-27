public class UpiPayment extends Payment {

    public int upi_id;

    protected UpiPayment(int paymentId, int amount, int upi_id) {
        super(paymentId, amount);
        this.upi_id = upi_id;
        
    }

    @Override
    public void processPayment() {
        System.out.println("processing payment via upi id");
    }
    
}
