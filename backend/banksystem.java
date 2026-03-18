package backend;

import java.util.ArrayList;

public class banksystem {

    private ArrayList<bankaccount> accounts = new ArrayList<>();

    public void createAccount(long accNo, String name) {
        accounts.add(new bankaccount(accNo, name));
    }

    public bankaccount findAccount(long accNo) {
        for (bankaccount a : accounts) {
            if (a.getAccNo() == accNo) {
                return a;
            }
        }
        return null;
    }
}