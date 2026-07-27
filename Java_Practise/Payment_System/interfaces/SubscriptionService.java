package Payment_System.interfaces;

import java.math.BigDecimal;

public interface SubscriptionService {
    void startSubscription();


    void endSubscription();

    void changeSubscriptionAmount(double newAmount);


    void changeSubscriptionAmount(BigDecimal newAmount);


    void changeSubscriptionAmount1(double newAmount);
}
