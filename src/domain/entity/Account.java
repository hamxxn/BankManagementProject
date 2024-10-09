public class Account {
    private String name;
    private String accountNum;
    private String accountpw;
    private int balance;

    public Account(String name, String accountNum, String accountpw, int balance) {
        this.name = name;
        this.accountNum = accountNum;
        this.accountpw = accountpw;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public String getAccountpw() {
        return accountpw;
    }

    public int getBalance() {
        return balance;
    }

}