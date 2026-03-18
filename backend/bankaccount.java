package backend;

public class bankaccount {

    private long accNo;
    private String name;
    private double balance;

    public bankaccount(long accNo, String name) {
        this.accNo = accNo;
        this.name = name;
        this.balance = 0;
    }

    public long getAccNo() {
        return accNo;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amt) {
        balance += amt;
    }

    public boolean withdraw(double amt) {
        if (balance >= amt) {
            balance -= amt;
            return true;
        }
        return false;
    }
}