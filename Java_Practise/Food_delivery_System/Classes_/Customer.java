package Food_delivery_System.Classes_;

import java.util.UUID;

public class Customer {
    
    //parameters

    private UUID id;
    private String name;
    private Number walletBalance;

    // constructor

    public Customer(UUID id, String name, Number walletBalance){
        this.name  = name;
        this.id = UUID.randomUUID();
        this.walletBalance = walletBalance;
    }

    // getters and setters

    public UUID getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public Number getWalletBalance(){
        return this.walletBalance;
    }

    // setters

    public Customer setName(String name){
        this.name  = name;
        return this;
    }

    public Customer setwalletBalance(Number walletBalance){
        this.walletBalance = walletBalance;
        return this;
    } 
}
