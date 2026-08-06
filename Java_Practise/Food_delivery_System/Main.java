
    package Food_delivery_System;

import java.time.LocalDate;
import java.util.UUID;

import Food_delivery_System.Classes_.*;
import Food_delivery_System.Exceptions.*;

public class Main {

    public static void main(String[] args) {

        MenuItems pizza = new MenuItems(UUID.randomUUID(), "Margherita Pizza", 300.0, true, "Main Course");
        MenuItems soda = new MenuItems(UUID.randomUUID(), "Cola", 60.0, true, "Beverage");
        MenuItems dessert = new MenuItems(UUID.randomUUID(), "Brownie", 120.0, false, "Dessert"); // unavailable

        Restaurant restaurant = new Restaurant("Tony's Pizzeria", 4.5f, false,
                new MenuItems[]{pizza, soda, dessert}, null);

        Customer customer = new Customer(UUID.randomUUID(), "Aarav", 500.0);

        OrderService orderService = new OrderService();

        System.out.println("=== Attempt 1: Restaurant is closed ===");
        Cart cart1 = new Cart();
        cart1.addItem(new CartItems(pizza.getId(), 1, pizza.getName(), pizza.getPrice(), pizza.getisAvailble(), pizza.getCategory()));
        try {
            orderService.placeOrder(restaurant, customer, cart1, null);
        } catch (RestaurantClosedException e) {
            System.out.println("Blocked as expected: " + e.getMessage());
        }

        restaurant.setIsOpen(true);

        System.out.println("\n=== Attempt 2: Trying to add an unavailable item ===");
        try {
            cart1.addItem(new CartItems(dessert.getId(), 1, dessert.getName(), dessert.getPrice(), dessert.getisAvailble(), dessert.getCategory()));
        } catch (ItemUnavailableException e) {
            System.out.println("Blocked as expected: " + e.getMessage());
        }

        System.out.println("\n=== Attempt 3: Successful order with a Flat coupon ===");
        Cart cart2 = new Cart();
        cart2.addItem(new CartItems(pizza.getId(), 1, pizza.getName(), pizza.getPrice(), pizza.getisAvailble(), pizza.getCategory()));
        cart2.addItem(new CartItems(soda.getId(), 2, soda.getName(), soda.getPrice(), soda.getisAvailble(), soda.getCategory()));
        System.out.println("Cart subtotal: ₹" + cart2.getTotal());

        FlatCoupen flat50 = new FlatCoupen(UUID.randomUUID(), LocalDate.now().plusDays(5), 200.0, 50.0);
        Order order1 = orderService.placeOrder(restaurant, customer, cart2, flat50);
        System.out.println(order1);
        System.out.println("Customer wallet balance now: ₹" + customer.getWalletBalance());

        System.out.println("\n=== Attempt 4: Order lifecycle progression ===");
        orderService.advanceOrderStatus(order1);
        System.out.println("Status -> " + order1.getOrderStatus());
        orderService.advanceOrderStatus(order1);
        System.out.println("Status -> " + order1.getOrderStatus());
        orderService.advanceOrderStatus(order1);
        System.out.println("Status -> " + order1.getOrderStatus());

        System.out.println("\n=== Attempt 5: Percentage coupon on a fresh cart ===");
        Cart cart3 = new Cart();
        cart3.addItem(new CartItems(pizza.getId(), 1, pizza.getName(), pizza.getPrice(), pizza.getisAvailble(), pizza.getCategory()));
        PercentageCoupon percent10 = new PercentageCoupon(UUID.randomUUID(), 100.0, LocalDate.now().plusDays(5));
        percent10.setPercentage(10);
        Order order2 = orderService.placeOrder(restaurant, customer, cart3, percent10);
        System.out.println(order2 + " (10% off ₹" + cart3.getTotal() + ")");

        System.out.println("\n=== Attempt 6: Buy-One-Get-One coupon, registered against pizza only ===");
        Cart cart4 = new Cart();
        cart4.addItem(new CartItems(pizza.getId(), 1, pizza.getName(), pizza.getPrice(), pizza.getisAvailble(), pizza.getCategory()));
        BuyOneGetOneCoupon bogo = new BuyOneGetOneCoupon(UUID.randomUUID(), LocalDate.now().plusDays(5));
        restaurant.createNewCoupen(bogo, pizza); // registers eligibility with the shared CoupenManager
        Order order3 = orderService.placeOrder(restaurant, customer, cart4, bogo);
        System.out.println(order3 + " (BOGO half-off applied)");

        System.out.println("\n=== Attempt 7: Payment fails -> rollback ===");
        Customer poorCustomer = new Customer(UUID.randomUUID(), "Zoya", 50.0);
        Cart cart5 = new Cart();
        cart5.addItem(new CartItems(pizza.getId(), 1, pizza.getName(), pizza.getPrice(), pizza.getisAvailble(), pizza.getCategory()));
        try {
            orderService.placeOrder(restaurant, poorCustomer, cart5, null);
        } catch (PaymentFailedException e) {
            System.out.println("Payment failed as expected: " + e.getMessage());
            System.out.println("Zoya's wallet balance unchanged: ₹" + poorCustomer.getWalletBalance());
        }
    }
}

