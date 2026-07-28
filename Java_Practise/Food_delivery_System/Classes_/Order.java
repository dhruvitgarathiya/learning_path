package Food_delivery_System.Classes_;

import java.util.UUID;

import Food_delivery_System.Enum.OrderStatus;

public class Order {
    // parameters

    private UUID orderId;
    private CartItems items;
    private Number total_amount;
    private OrderStatus status ;

    // constructor

    public Order(UUID orderId, CartItems items, Number total_amount,OrderStatus status){
        this.orderId = orderId;
        this.items = items;
        this.status = status;
        this.total_amount = total_amount;
    }

    // 

    
    
}
