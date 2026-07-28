
import java.math.BigDecimal;
import java.time.LocalDate;

import Payment_System.Enums.SubscriptionStatus;
import Payment_System.interfaces.SubscriptionService;

/**
 * Concrete implementation of Razorpay.
 *
 * Inherits all common state from PaymentGateway
 * and provides its own implementation for
 * subscription operations.
 */
public class Razorpay extends PaymentGatway
implements SubscriptionService {

    /* -------------------------------
       Razorpay Specific State
       ------------------------------- */

    private String merchantId;

    private String apiKey;

    /* -------------------------------
       Constructor
       ------------------------------- */

    public Razorpay(String merchantId,
                    String apiKey,
                    BigDecimal subscriptionAmount) {

        super();

        this.merchantId = merchantId;
        this.apiKey = apiKey;
    }

    /* -------------------------------
       Interface Implementation
       ------------------------------- */

    @Override
    public void startSubscription() {

        setSubscriptionStartDate(LocalDate.now());

        setStatus(SubscriptionStatus.ACTIVE);

        System.out.println(
                getGatewayName() +
                " subscription started.");

    }

    private void setStatus(SubscriptionStatus active) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStatus'");
    }

    private void setSubscriptionStartDate(LocalDate now) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSubscriptionStartDate'");
    }

    @Override
    public void endSubscription() {

        setSubscriptionStartDate(LocalDate.now());

        setStatus(SubscriptionStatus.CANCELED);

        System.out.println(
                getGatewayName() +
                " subscription ended.");

    }

    private String getGatewayName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGatewayName'");
    }

    @Override
    public void changeSubscriptionAmount1(double newAmount) {

        if (newAmount <= 0) {

            throw new IllegalArgumentException(
                    "Amount must be greater than zero.");
        }

        changeSubscriptionAmount(newAmount);

        System.out.println(
                "Subscription updated to $" +
                newAmount);

    }

    /* -------------------------------
       Razorpay Specific Methods
       ------------------------------- */

    public void generateApiToken() {

        System.out.println(
                "Generating Razorpay API Token...");

    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public void changeSubscriptionAmount(double newAmount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeSubscriptionAmount'");
    }

    @Override
    public void changeSubscriptionAmount(BigDecimal newAmount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeSubscriptionAmount'");
    }

}