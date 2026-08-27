public abstract class Payment{
    public int PaymentId;
    public int amount;

    protected Payment(int paymentId, int amount){
        this.PaymentId = paymentId;
        this.amount = amount;
    }

    public abstract void processPayment();

    public void printReceipt(){
        System.out.println("thank you for the payment of" + amount);
    }
}

