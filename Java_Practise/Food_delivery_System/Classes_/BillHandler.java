package Food_delivery_System.Classes_;

import Food_delivery_System.Exceptions.InvalidCouponException;
import Food_delivery_System.Interfaces.BillFunctions;

public class BillHandler implements BillFunctions {

    @Override
    public double calculateTotalDiscount(Cart c, Coupen cp) {
        if (cp == null) {
            return 0.0;
        }

        if (cp.isExpired()) {
            throw new InvalidCouponException("Coupon " + cp.getUniqueCode() + " has expired.");
        }

        
        double discount = cp.getDiscountAmount(c);

        double subtotal = c.getTotal();
        return Math.min(discount, subtotal);
    }
}