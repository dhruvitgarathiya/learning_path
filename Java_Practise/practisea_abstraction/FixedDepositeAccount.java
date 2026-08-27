public class FixedDepositeAccount extends BankAccount{

    public FixedDepositeAccount(int account_number, String holder_name, double balance) {
        super(account_number, holder_name, balance);
    }

    @Override
    public double calculateIntrest() {
        return intrest = (0.07 * balance);
    }

    @Override
    public void withdraw(int a) {
        System.out.println("you cannot withdraw from fixed deposite account");
    }
    
}
