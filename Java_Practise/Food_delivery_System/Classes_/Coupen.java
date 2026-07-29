package Food_delivery_System.Classes_;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Coupen{
    // parameters

    private UUID unique_code;
    private double discount_value;
    private LocalDate expiration_date;
    private double min_amount;

    // constructor

    public Coupen(UUID unique_code, LocalDate expiration_date){
        this(unique_code, expiration_date, 0);
    }
    public Coupen(UUID unique_code,LocalDate expiration_date, double min_amount){
        this(unique_code, expiration_date,min_amount, 0);
    }

    public Coupen(UUID unique_code,  LocalDate expiration_date,double min_amount ,double discount_value){
        this.unique_code = unique_code;
        this.discount_value = discount_value;
        this.expiration_date = expiration_date;
        this.min_amount = min_amount;
    }

    // getters and setters

    public UUID getUniqueCode(){
       return  this.unique_code;
    }

    public Number getDiscountvalue(){
        return this.discount_value;
    }

    public LocalDate getExpirationDate(){
        return this.expiration_date;
    }

    public Number getMinamount(){
        return this.min_amount;
    }


    public Coupen setUniqueCode(UUID unique_code){
        this.unique_code = unique_code;
        return this;
    }

    public Coupen setDiscountValue(double discount_value){
        this.discount_value = discount_value;
        return this;
    }

    public Coupen setExpirationDate(LocalDate expiration_date){
        this.expiration_date = expiration_date;
        return this;
    }

    public Coupen setMinAmount(double min_amount){
        this.min_amount = min_amount;
        return this;
    }
    

}