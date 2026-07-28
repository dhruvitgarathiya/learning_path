package Payment_System.Services;


import Payment_System.interfaces.SubscriptionService;

/**
 * Handles subscription operations.
 *
 * Notice this class depends on
 * an interface instead of concrete classes.
 */
public class PaymentManager{

    /**
     * Starts subscription.
     */
    public void start(SubscriptionService gateway){

        gateway.startSubscription();

    }

    /**
     * Ends subscription.
     */
    public void end(SubscriptionService gateway){

        gateway.endSubscription();

    }

    /**
     * Updates subscription price.
     */
    public void updateAmount(
            SubscriptionService gateway,
            double amount){

        gateway.changeSubscriptionAmount(amount);

    }

}