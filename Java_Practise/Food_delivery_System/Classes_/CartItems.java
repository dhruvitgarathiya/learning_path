package Food_delivery_System.Classes_;

import java.util.UUID;


public class CartItems extends MenuItems  {
    
    // parameters

    private double quantity;

    // constructer

    public CartItems(UUID id, double quantity, String name, double price, boolean isAvailble, String category ){

        super(id, name, price, isAvailble,category);
        this.quantity = quantity;
    }

    // getters and setters


    public double getQuantity(){
        return this.quantity;
    }


    public CartItems SetQuantity(Number quantity){
        return this;
    }

   
  
}

