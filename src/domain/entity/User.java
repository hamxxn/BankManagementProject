package domain.entity;

import domain.dto.AccountInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private String id;
    private String username;
    private int password;
    private String birth;
    private String phoneNum;
    private List<AccountInfo> accounts;

    public User(String id, int password, String username, String phoneNum, String birth) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.phoneNum= phoneNum;
        this.birth = birth;
        this.accounts = new ArrayList<AccountInfo>();
    }
}
