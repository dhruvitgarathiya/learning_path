package Food_delivery_System.Classes_;

import java.util.HashMap;
import java.util.Map;

import Food_delivery_System.Interfaces.CoupenHandler;

public class CoupenManager implements CoupenHandler {
    
    private final Map<MenuItems, Coupen> CoupenEligibility = new HashMap<>();
    @Override
    public void registerCoupen(Restaurant r, Coupen bg, MenuItems mi){

            CoupenEligibility.put(mi,bg);

    }
    @Override
    public boolean checkCoupenApplicability(Coupen bg, MenuItems m){

        if(CoupenEligibility.containsKey(bg) == false){
            return false;
            // coupen dont exists
        }

        for(Map.Entry<MenuItems, Coupen> entry : CoupenEligibility.entrySet()){
            if(entry.getKey() == m){
                if(entry.getValue() == bg){
                    return true;
                }
            }
        }
        return false;
    }

    
}
