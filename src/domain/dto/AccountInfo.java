package domain.dto;

// TODO AccountInfo 텍스트파일에 있는 정보 다 받아올 수 있도록 해야 함. 이름, 계좌번호, 비번, 잔액
public class AccountInfo {
    private String accountNum;
    private int balance;

    public AccountInfo(String accountNum, int balance) {
        this.accountNum = accountNum;
        this.balance = balance;
    }
}

// Todo 아래로 쭉
//