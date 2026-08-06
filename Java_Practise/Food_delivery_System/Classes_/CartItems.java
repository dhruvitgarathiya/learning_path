package Food_delivery_System.Classes_;

import java.util.UUID;

public class CartItems extends MenuItems {

    // parameters

    private double quantity;

    // constructor

    public CartItems(UUID id, double quantity, String name, double price, boolean isAvailble, String category) {
        super(id, name, price, isAvailble, category);
        this.quantity = quantity;
    }

    // getters and setters

    public double getQuantity() {
        return this.quantity;
    }

    public CartItems SetQuantity(Number quantity) {
        // BUG FIX: this used to just `return this;` without ever touching
        // the field, so quantity could never actually change.
        this.quantity = quantity.doubleValue();
        return this;
    }

    @Override
    public String toString() {
        return getName() + " x" + quantity + " = ₹" + (quantity * getPrice());
    }
}