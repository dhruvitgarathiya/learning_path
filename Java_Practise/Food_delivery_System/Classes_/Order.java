package Food_delivery_System.Classes_;

import java.util.UUID;

import Food_delivery_System.Enum.OrderStatus;

public class Order {
    // parameters

    private UUID orderId;
    private Cart CartSummery;
    private double total_amount;
    private OrderStatus status ;

    // constructor

    public Order(UUID orderId, Cart CartSummery, double total_amount,OrderStatus status){
        this.orderId = orderId;
        this.CartSummery = CartSummery;
        this.status = status;
        this.total_amount = total_amount;
    }

    //methods
    
    public UUID getOrderId(){
        return this.orderId;
    }

    public OrderStatus getOrderStatus(){
        return this.status;
    }

    public double getTotalAmount(){
        return this.total_amount;
    }

    public Order setOrderStatus(OrderStatus status){
        this.status = status;
        return this;
    }

    public Order setOrderTotalAmount(double amount){
        this.total_amount = amount;
        return this;
    }

   
    

    
    
}
