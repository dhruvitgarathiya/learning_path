package Food_delivery_System.Classes_;

import java.util.UUID;

public class MenuItems {
    
    // parameters

    private UUID id;
    private String name;
    private double price;
    private boolean isAvailble;
    private String category;


    // constructor
    public MenuItems(UUID id, String name, double price, boolean isAvailble, String category){
        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.isAvailble = isAvailble;
        this.category = category;
    }

    // getters and setters

    public UUID getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public double getPrice(){
        return this.price;

    }

    public boolean getisAvailble(){
        return this.isAvailble;
    }

    public String getCategory(){
        return this.category;
    }

    // setters

    public MenuItems setName(String name){
        this.name  = name;
        return this;
    }

    public MenuItems setPrice(double price){
        this.price = price;
        return this;
    }

    public MenuItems setIsAvailble(boolean isAvailble){
        this.isAvailble = isAvailble;
        return this;
    }

    public MenuItems setCategory(String category){
        this.category = category;
        return this;
    }

}
