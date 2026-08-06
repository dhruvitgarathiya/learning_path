package Food_delivery_System.Classes_;

import java.time.LocalDate;
import java.util.UUID;

import Food_delivery_System.Interfaces.CoupenFunctions;

public class BuyOneGetOneCoupon extends Coupen implements CoupenFunctions<MenuItems, Double> {

    public BuyOneGetOneCoupon(UUID unique_code, LocalDate expiration_date) {
        super(unique_code, expiration_date);
    }

    // methods

    @Override
    public Double calculateDiscount(MenuItems i) {
        double p = i.getPrice();
        CoupenManager cm = CoupenManager.getInstance();
        if (cm.checkCoupenApplicability(this, i) == true) {
            return p / 2;
        }

        return 0.0;
    }

    @Override
    public double getDiscountAmount(Cart cart) {
        double discount = 0.0;
        for (CartItems item : cart.getItems()) {
            discount += calculateDiscount(item);
        }
        return discount;
    }
}