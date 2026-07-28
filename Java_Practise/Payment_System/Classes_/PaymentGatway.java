package Payment_System.Classes_;
import java.time.LocalDate;

import Payment_System.Enums.SubscriptionStatus;

public class PaymentGatway {

    // parmeters

    private String gatewayName;

    private double subscriptionAmount;

    private LocalDate subscriptionStartDate;

    private LocalDate subscriptionEndDate;

    private SubscriptionStatus status;

    // constructor

    protected PaymentGatway(String gatewayName, double subscriptionAmount){

        this.gatewayName = gatewayName;

        this.subscriptionAmount = subscriptionAmount;

        this.status  = SubscriptionStatus.INACTIVE;
    }


    // getters

     public String getGatewayName() {
        return gatewayName;
    }

    public double getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public LocalDate getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public LocalDate getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    // setters

    protected void setSubscriptionAmount(double amount){
        this.subscriptionAmount = amount;
    }

    protected void setSubscriptionStartDate(LocalDate startDate){
        this.subscriptionStartDate = startDate;
    }

    protected void setSubscriptionEndDate(LocalDate endDate){
        this.subscriptionEndDate = endDate;
    }

    protected void setStatus(SubscriptionStatus status){
        this.status = status;
    }

}
