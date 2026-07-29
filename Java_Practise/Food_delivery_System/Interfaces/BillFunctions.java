package Food_delivery_System.Interfaces;

import Food_delivery_System.Classes_.Cart;
import Food_delivery_System.Classes_.Coupen;

public interface BillFunctions {
    public void calculateTotalDiscount(Cart c,Coupen cp);
}
