package Payment_System;



import Payment_System.Classes.Razorpay;
import Payment_System.Services.PaymentManager;

public class Client {

    public static void main(String[] args) {

        Razorpay razorpay = new Razorpay(

                "MERCHANT123",

                "API_KEY",

                499.99

        );

        PaymentManager manager =
                new PaymentManager();

        manager.start(razorpay);

        manager.updateAmount(

                razorpay,

                new BigDecimal("999.99")

        );

        manager.end(razorpay);

    }

}