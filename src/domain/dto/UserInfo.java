package domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserInfo {
    private String username;
    private String password;
    private String birth;
    private String id;
    private String phoneNumber;
    private List<AccountInfo> accounts;
}
