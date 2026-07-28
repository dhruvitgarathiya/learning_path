package Food_delivery_System.Classes_;

public class Restaurant {
    //parameters

    private String name;
    private float rating;
    private boolean isOpen;
    private MenuItems[] menu;
    

    // constructor

    public Restaurant(String name, float rating, boolean isOpen){
        this(name, rating, isOpen, null);
    }

    public Restaurant(String name, float rating ,boolean isOpen,MenuItems[] menu){
        this.name = name;
        this.rating = rating;
        this.isOpen = isOpen;
        this.menu = menu;
    }

    // getters and setters

     // getters
    
     public String getName(){
        return this.name;
     }

     public Float getRating(){
        return this.rating;
     }

     public boolean getIsOpen(){
        return this.isOpen;
     }

     // setters

     public Restaurant setNamw(String name){
        this.name  = name;
        return this;
     }

     public Restaurant setRating(float rating){
        this.rating = rating;
        return this;
     }

     public Restaurant setIsOpen(boolean isOpen){
        this.isOpen = isOpen;
        return this;
     }

    // methods

    
}
