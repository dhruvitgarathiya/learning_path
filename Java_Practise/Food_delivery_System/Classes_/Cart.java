package Food_delivery_System.Classes_;

import java.util.ArrayList;
import java.util.List;

import Food_delivery_System.Exceptions.ItemUnavailableException;
import Food_delivery_System.Interfaces.CartFunctions;


public class Cart implements CartFunctions {

    private final List<CartItems> items = new ArrayList<>();

    public List<CartItems> getItems() {
        return items;
    }

    @Override
    public void addItem(CartItems i) {
        if (!i.getisAvailble()) {
            throw new ItemUnavailableException("Cannot add '" + i.getName() + "' to cart: item is unavailable.");
        }
        items.add(i);
    }

    @Override
    public void removeItem(CartItems i) {
        items.remove(i);
    }

    @Override
    public void changeQuantity(CartItems i, double new_quantity) {
        if (new_quantity <= 0) {
            items.remove(i);
            return;
        }
        i.SetQuantity(new_quantity);
    }

    @Override
    public double getTotal(CartItems[] p) {
        double total = 0;
        for (CartItems i : p) {
            total += (i.getQuantity()) * (i.getPrice());
        }
        return total;
    }
    public double getTotal() {
        return getTotal(items.toArray(new CartItems[0]));
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}