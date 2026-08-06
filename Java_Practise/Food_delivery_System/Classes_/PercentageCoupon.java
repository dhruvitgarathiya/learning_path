package Food_delivery_System.Classes_;

import java.time.LocalDate;
import java.util.UUID;

import Food_delivery_System.Interfaces.CoupenFunctions;

public class PercentageCoupon extends Coupen implements CoupenFunctions<Double, Double> {

    public PercentageCoupon(UUID unique_code, double min_amount, LocalDate expiration_date) {
        super(unique_code, expiration_date, min_amount);
    }

    // parameter
    private Number percentage;

    // methods

    public Number getPercentage() {
        return this.percentage;
    }

    public PercentageCoupon setPercentage(Number percentage) {
        this.percentage = percentage;
        return this;
    }

    @Override
    public Double calculateDiscount(Double input) {
        double i = input;

        if (i >= this.getMinamount().doubleValue()) {
            return input * (this.getPercentage().doubleValue() / 100);
        }

        return 0.0;
    }

    @Override
    public double getDiscountAmount(Cart cart) {
        return calculateDiscount(cart.getTotal());
    }
}