public  abstract class FoodOrder {
    public int orderId;
    public String customerName;
    public int amount;
    protected double final_amount;
    protected double delivery_charge;

    public FoodOrder(int orderId, String customerName, int amount){
        this.orderId = orderId;
        this.customerName = customerName;
        this.amount = amount;
    }
    public abstract double calculateDeliveryCharge();
    public double calcualteFinalAmount(){
        delivery_charge = calculateDeliveryCharge();
        return final_amount = delivery_charge + amount;
    };
    public void placeOrder(){
        System.out.println("order placed");
    }

}
