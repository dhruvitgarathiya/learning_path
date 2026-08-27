public class CashPayment extends Payment{

    protected CashPayment(int paymentId, int amount) {
        super(paymentId, amount);
        
    }

    @Override
    public void processPayment() {
        System.out.println("processing payment via cash");
    }
    
}