package domain.dto;

// TODO 이름, 계좌 비번
public class AccountInfo {
    private String accountNum;
    private int balance;

    public AccountInfo(String accountNum, int balance) {
        this.accountNum = accountNum;
        this.balance = balance;
    }

}
