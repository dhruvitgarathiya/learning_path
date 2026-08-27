public class SavingAccount extends BankAccount{

    public SavingAccount(int account_number, String holder_name, double balance) {
        super(account_number, holder_name, balance);
    }

    @Override
    public double calculateIntrest() {
        return intrest = (0.05 * balance);
    }

}

