package Food_delivery_System.Classes_;

import java.util.UUID;

import Food_delivery_System.Enum.OrderStatus;
import Food_delivery_System.Exceptions.InsufficientBalanceException;
import Food_delivery_System.Exceptions.ItemUnavailableException;
import Food_delivery_System.Exceptions.PaymentFailedException;
import Food_delivery_System.Exceptions.RestaurantClosedException;

public class OrderService {

    private final BillHandler billHandler = new BillHandler();

    public Order placeOrder(Restaurant restaurant, Customer customer, Cart cart, Coupen coupon) {

        if (!restaurant.getIsOpen()) {
            throw new RestaurantClosedException(
                    "Cannot place order: '" + restaurant.getName() + "' is currently closed.");
        }

        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Cannot place an order with an empty cart.");
        }

        for (CartItems item : cart.getItems()) {
            if (!item.getisAvailble()) {
                throw new ItemUnavailableException(
                        "Cannot place order: '" + item.getName() + "' is no longer available.");
            }
        }

        double subtotal = cart.getTotal();
        double discount = (coupon != null) ? billHandler.calculateTotalDiscount(cart, coupon) : 0.0;
        double finalAmount = Math.max(subtotal - discount, 0.0);

        Order order = new Order(UUID.randomUUID(), cart, finalAmount, OrderStatus.CREATED);

        try {
            processPayment(customer, order);
        } catch (InsufficientBalanceException e) {
            // Rollback: order never gets confirmed, no money moves.
            order.setOrderStatus(OrderStatus.CANCELLED);
            throw new PaymentFailedException(
                    "Payment failed, order " + order.getOrderId() + " has been cancelled. Reason: " + e.getMessage());
        }

        order.setOrderStatus(OrderStatus.CONFIRMED);
        return order;
    }

    private void processPayment(Customer customer, Order order) {
        double balance = customer.getWalletBalance().doubleValue();
        double amount = order.getTotalAmount();

        if (balance < amount) {
            throw new InsufficientBalanceException(
                    "Insufficient wallet balance. Required: ₹" + amount + ", Available: ₹" + balance);
        }

        // Wallet is only ever deducted here, after the balance check
        // succeeds - i.e. only on successful payment.
        customer.setwalletBalance(balance - amount);
    }

    public Order advanceOrderStatus(Order order) {
        OrderStatus current = order.getOrderStatus();
        switch (current) {
            case CONFIRMED:
                order.setOrderStatus(OrderStatus.PREPARING);
                break;
            case PREPARING:
                order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
                break;
            case OUT_FOR_DELIVERY:
                order.setOrderStatus(OrderStatus.DELIVERED);
                break;
            default:
                throw new IllegalStateException("Cannot advance order from state: " + current);
        }
        return order;
    }

    public Order cancelOrder(Order order) {
        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel an order that has already been delivered.");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return order;
    }
}