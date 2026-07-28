package Payment_System.Classes_;

import java.time.LocalDate;

import Payment_System.Enums.SubscriptionStatus;
import Payment_System.interfaces.SubscriptionService;

public class Razorpay extends PaymentGatway implements SubscriptionService {

    // razorpay specific parameters

    private int merchentId;

    private String APIkey;


    // constructor

    public Razorpay(String GatewayName, int merchentId, String APIkey, double SubscriptionAmount){
        
        super(GatewayName, SubscriptionAmount);

        this.merchentId = merchentId;

        this.APIkey = APIkey;
        
    }

    // Interface implementation

    @Override
    public void startSubscription(){

        setSubscriptionStartDate(LocalDate.now());

        setStatus(SubscriptionStatus.ACTIVE);

        System.out.println("Subscription of"+ getGatewayName() + "have been started today");
    }
    
    @Override
    public void endSubscription(){
        setSubscriptionEndDate(LocalDate.now());

        setStatus(SubscriptionStatus.INACTIVE);

        System.out.println("Subscription of"+ getGatewayName() +"have been ended today");
    }

    @Override
    public void changeSubscriptionAmount(double NewAmount){

        if(NewAmount <= 0){
            System.err.println("Enter valid subscription amount");
        }

        setSubscriptionAmount(NewAmount);

        System.out.println("Subscription amount changed");

    }

    // razorpay specific methods

    // getter methods for the razorpay

    public int getMerchentId(){
        return merchentId;
    }

    public String getAPIKey(){
        return APIkey;
    }


}