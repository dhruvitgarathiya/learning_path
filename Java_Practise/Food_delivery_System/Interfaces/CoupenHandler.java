package Food_delivery_System.Interfaces;

import Food_delivery_System.Classes_.Coupen;
import Food_delivery_System.Classes_.MenuItems;
import Food_delivery_System.Classes_.Restaurant;

public interface CoupenHandler {
    public void registerCoupen(Restaurant r,Coupen c, MenuItems m);
    public boolean checkCoupenApplicability(Coupen c, MenuItems m);
   
}
