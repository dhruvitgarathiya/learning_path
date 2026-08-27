public class ExpressOrder extends FoodOrder {

    public ExpressOrder(int orderId, String customerName, int amount) {
        super(orderId, customerName, amount);
       
    }

    @Override
    public double calculateDeliveryCharge() {
        return delivery_charge = 100;
    }
    
}
