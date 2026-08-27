public abstract class BankAccount {
    protected int account_number;
    protected String holder_name;
    protected double balance;
    protected double intrest;

    public BankAccount(int account_number, String holder_name, double balance){
        this.account_number = account_number;
        this.holder_name = holder_name;
        this.balance = balance;
    }

    public void deposite(int a){
        balance += a;
        System.out.println(a +"amount is being deposited, now your balance is: "+ balance);
    }

    public void withdraw(int a){
        balance -= a;
        System.out.println(a + "amount is being withdrawned , now your balance is: " + balance);
    }

    public abstract double calculateIntrest();
}
