package Payment_System;

import Payment_System.Classes_.Razorpay;
import Payment_System.Services.PaymentManager;

public class Client{

    public static void main(String[] args){

        PaymentManager manager = new PaymentManager();

        Razorpay r1 = new Razorpay("Razorpay_1", 503, null, 32000);

        manager.start(r1);
        manager.updateAmount(r1, 40000);
        manager.end(r1);
    }
}