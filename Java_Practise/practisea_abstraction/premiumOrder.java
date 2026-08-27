public class premiumOrder extends FoodOrder {

    public premiumOrder(int orderId, String customerName, int amount) {
        super(orderId, customerName, amount);
       
    }

    @Override
    public double calculateDeliveryCharge() {
        return delivery_charge = 0;
    }
    
}
