package Food_delivery_System.Interfaces;

import Food_delivery_System.Classes_.Cart;
import Food_delivery_System.Classes_.Coupen;

public interface BillFunctions {
    public double calculateTotalDiscount(Cart c,Coupen cp);
}
