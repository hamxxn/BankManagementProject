package domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AccountInfo {
    private String accountNum;
    private int balance;
}
