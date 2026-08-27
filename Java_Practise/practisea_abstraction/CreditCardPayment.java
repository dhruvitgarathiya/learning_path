public class CreditCardPayment extends Payment {

    public int credit_card_number;

    protected CreditCardPayment(int paymentId, int amount, int credit_card_number) {
        super(paymentId, amount);
        this.credit_card_number = credit_card_number;
    }

    @Override
    public void processPayment() {
        System.out.println("processing payment via credit card");
    }
    
}
