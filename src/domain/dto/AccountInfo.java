package domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AccountInfo {
    private String accountNum;
    private int balance;
    private int accountpw;

    public AccountInfo(String accountNum, int balance, int accountpw) {
        this.accountNum = accountNum;
        this.balance = balance;
        this.accountpw = accountpw;
    }

}
