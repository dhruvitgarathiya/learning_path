package Payment_System.interfaces;

import Payment_System.Classes_.PaymentGatway;

// we are thinking of creating an method where if one subscription is ended for user then another starts automatially

public interface ChangingSubscription {

     PaymentGatway[] availableSubscriptions(PaymentGatway GatewayName);
    
     PaymentGatway switchTheSubscription(PaymentGatway[] args);



}
