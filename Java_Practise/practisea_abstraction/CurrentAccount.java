public class CurrentAccount  extends BankAccount{

    public CurrentAccount(int account_number, String holder_name, double balance) {
        super(account_number, holder_name, balance);
        //TODO Auto-generated constructor stub
    }

    @Override
    public double calculateIntrest() {
       return intrest = 0;
    }
    
}
