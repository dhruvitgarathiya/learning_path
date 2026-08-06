package Food_delivery_System.Classes_;

import java.util.HashMap;
import java.util.Map;

import Food_delivery_System.Interfaces.CoupenHandler;

public class CoupenManager implements CoupenHandler {

    
    private static final CoupenManager INSTANCE = new CoupenManager();

    public static CoupenManager getInstance() {
        return INSTANCE;
    }

    private final Map<Coupen, MenuItems> CoupenEligibility = new HashMap<>();

    @Override
    public void registerCoupen(Restaurant r, Coupen bg, MenuItems mi) {
        CoupenEligibility.put(bg, mi);
    }

    @Override
    public boolean checkCoupenApplicability(Coupen bg, MenuItems m) {

        if (CoupenEligibility.containsKey(bg) == false) {
            return false;
            // coupen dont exists
        }

        for (Map.Entry<Coupen, MenuItems> entry : CoupenEligibility.entrySet()) {
            if (entry.getKey() == bg) {
                if (entry.getValue() == m) {
                    return true;
                }
            }
        }
        return false;
    }
}