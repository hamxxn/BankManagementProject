package domain.dto;


public class AccountInfo {
    private String accountNum;
    private int balance;

    public AccountInfo(String accountNum, int balance) {
        this.accountNum = accountNum;
        this.balance = balance;
    }
    public String getAccountNum() {
        return accountNum;
    }

}
