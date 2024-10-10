package domain.dto;

// TODO AccountInfo 텍스트파일에 있는 정보 다 받아올 수 있도록 해야 함. 이름, 계좌번호, 비번, 잔액
public class AccountInfo {
    private String name;
    private String accountNum;
    private String accountPw;
    private int balance;


    public AccountInfo(String name, String accountNum, String accountPw, int balance) {
        this.name = name;
        this.accountNum = accountNum;
        this.accountPw = accountPw;
        this.balance = balance;
    }

    public String getAccountNum() {
        return accountNum;
    }
}



