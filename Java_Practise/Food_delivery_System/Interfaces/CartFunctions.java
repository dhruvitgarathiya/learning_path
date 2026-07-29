package Food_delivery_System.Interfaces;

import Food_delivery_System.Classes_.CartItems;


public interface CartFunctions {
    
    public void addItem(CartItems i);

    public void removeItem(CartItems i);

    public void changeQuantity(CartItems i,double new_quantity);

    public double getTotal(CartItems[] p);
}
