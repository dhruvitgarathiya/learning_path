package Food_delivery_System.Classes_;

import java.util.UUID;

public class CartItems {
    
    // parameters

    private UUID id;
    private MenuItems item;
    private Number quantity;

    // constructer

    public CartItems(UUID id, MenuItems item, Number quantity ){
        this.id = UUID.randomUUID();
        this.item = item;
        this.quantity = quantity;
    }

    // getters and setters

    public UUID getId(){
        return this.id;
    }

    public MenuItems getItem(){
        return this.item;
    }

    public Number getQuantity(){
        return this.quantity;
    }

    public CartItems SetItem(MenuItems item){
        return this;
    }

    public CartItems SetQuantity(Number quantity){
        return this;
    }
  
}

