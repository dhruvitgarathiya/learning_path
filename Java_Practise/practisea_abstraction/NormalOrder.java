public class NormalOrder extends FoodOrder {

    public NormalOrder(int orderId, String customerName, int amount ){
        super(orderId, customerName, amount);
        
    }

    @Override
    public double calculateDeliveryCharge() {
        return delivery_charge = 40;
    }
    
}
