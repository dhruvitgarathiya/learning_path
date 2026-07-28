package Payment_System.interfaces;

public interface SubscriptionService {
    void startSubscription();

    void endSubscription();

    void changeSubscriptionAmount(double newAmount);
    
}
