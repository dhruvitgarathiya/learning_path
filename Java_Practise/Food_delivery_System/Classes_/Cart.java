package Food_delivery_System.Classes_;

import java.util.ArrayList;
import java.util.UUID;

import Food_delivery_System.Interfaces.CartFunctions;

public class Cart extends CartItems implements CartFunctions {

    public Cart(UUID id, double quantity, String name, double price, boolean isAvailble, String category) {
        super(id, quantity, name, price, isAvailble, category);
    }

     @Override
    public void addItem(CartItems i) {
        if(i.getisAvailble() == true){
            this.SetQuantity(1);
        }
        return;
    }

    @Override
    public void removeItem(CartItems i) {
      
        if(this.getQuantity() > 0){
            this.SetQuantity(0);
        }
      
      return;
    }

    @Override
    public void changeQuantity(CartItems i,double new_quantity) {
    
            this.SetQuantity(new_quantity);
        
        return;
    }

    @Override
    public double getTotal(CartItems[] p) {
       double total = 0;
       for(CartItems i: p){
        total += (i.getQuantity()) * ((i.getPrice()));
       }

       return total;
    }



    
    
}
