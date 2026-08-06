package Food_delivery_System.Classes_;

import java.time.LocalDate;
import java.util.UUID;

import Food_delivery_System.Interfaces.CoupenFunctions;

public class FlatCoupen extends Coupen implements CoupenFunctions<Double, Double> {

    public FlatCoupen(UUID unique_code, LocalDate expiration_date, double min_amount, double discount_value) {
        super(unique_code, expiration_date, min_amount, discount_value);
    }

    @Override
    public Double calculateDiscount(Double amount) {
        double x = this.getMinamount().doubleValue();
        double d = this.getDiscountvalue().doubleValue();

        if (amount >= x) {
            return d;
        }

        return 0.0;
    }

    @Override
    public double getDiscountAmount(Cart cart) {
        return calculateDiscount(cart.getTotal());
    }
}