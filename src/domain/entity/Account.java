package domain.entity;

public class Account {
    private String name;
    private String accountNum;
    private String accountPw;
    private int balance;
    private String lastTransferDate;

    public Account(String name, String accountNum, String accountPw, int balance) {
        this.name = name;
        this.accountNum = accountNum;
        this.accountPw = accountPw;
        this.balance = balance;
        this.lastTransferDate = "1";
    }

    public String getName() {
        return name;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public String getAccountPw() {
        return accountPw;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getLastTransferDate() {
        return lastTransferDate;
    }
    public void setLastTransferDate(String lastTransferDate) {
        this.lastTransferDate = lastTransferDate;
    }

}