package Food_delivery_System.Classes_;

import java.util.ArrayList;
import java.util.Arrays;

import Food_delivery_System.Interfaces.CoupenHandler;

public class Restaurant {
    // parameters

    private String name;
    private float rating;
    private boolean isOpen;
    private ArrayList<MenuItems> menu;
    private CoupenHandler coupenHandler;

    // constructor

    public Restaurant(String name, float rating, boolean isOpen) {
        this(name, rating, isOpen, null, null);
    }

    public Restaurant(String name, float rating, boolean isOpen, MenuItems[] menu, CoupenHandler coupenHandler) {
        this.name = name;
        this.rating = rating;
        this.isOpen = isOpen;
        this.menu = (menu != null) ? new ArrayList<>(Arrays.asList(menu)) : new ArrayList<>();
        this.coupenHandler = (coupenHandler != null) ? coupenHandler : CoupenManager.getInstance();
    }

    // getters and setters

    // getters

    public String getName() {
        return this.name;
    }

    public Float getRating() {
        return this.rating;
    }

    public boolean getIsOpen() {
        return this.isOpen;
    }

    public ArrayList<MenuItems> getMenu() {
        return this.menu;
    }

    // setters

    public Restaurant setNamw(String name) {
        this.name = name;
        return this;
    }

    public Restaurant setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public Restaurant setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
        return this;
    }

    // methods

    public void addItemToMenu(MenuItems m) {
        this.menu.add(m);
    }

    public void createNewCoupen(Coupen c, MenuItems m) {
        coupenHandler.registerCoupen(this, c, m);
    }
}