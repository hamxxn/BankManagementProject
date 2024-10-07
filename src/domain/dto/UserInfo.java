package domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class UserInfo {
    private String username;
    private int password;
    private String birth;
    private String id;
    private String phoneNum;
    private List<AccountInfo> accounts;
}
