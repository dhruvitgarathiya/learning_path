package Payment_System.Classes_;

import java.time.LocalDate;

import Payment_System.Enums.SubscriptionStatus;
import Payment_System.interfaces.SubscriptionService;

public class Stripe extends PaymentGatway implements SubscriptionService{

    // parameters

    private int stripeId;

    private String stringUrl;

    // constructor

    public Stripe(String GetwayName, double Amount, int stripeId, String stringUrl){

        super(GetwayName, Amount);

        this.stripeId = stripeId;

        this.stringUrl = stringUrl;
    }

    // interface methods
    @Override
    public void startSubscription(){
        
        setSubscriptionStartDate(LocalDate.now());

        setStatus(SubscriptionStatus.ACTIVE);

        System.out.print("subscription of your" + getGatewayName() + "is started");
        
    }


    @Override
    public void endSubscription(){

        setSubscriptionEndDate(LocalDate.now());

        setStatus(SubscriptionStatus.INACTIVE);

        System.out.println("subscription of your" + getGatewayName() + "is ended");
    }

    @Override
    public void changeSubscriptionAmount(double newAmoun){
        
        setSubscriptionAmount(newAmoun);

        System.out.println("amount of subcription have been changed");
    }

    // stripe specific methods

    public String getAPI(){

        return "xxxxxxx";
    }

    public int getstripeId(){
        return stripeId;
    }

    public String stingUrl(){
        return stringUrl;
    }
    
}
