package domain.entity;

import domain.dto.AccountInfo;
import java.util.ArrayList;

public class User {
    private String id;
    private String username;
    private String password;
    private String birth;
    private String phoneNum;
    private String accountNum;

    public User(String id, String password, String username, String phoneNum, String birth, String accountNum) {

    private List<Account> accounts;

    public User(String id, String password, String username, String phoneNum, String birth,ArrayList<Account> accounts) {

        this.id = id;
        this.password = password;
        this.username = username;
        this.phoneNum= phoneNum;
        this.birth = birth;
        this.accountNum = accountNum;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getPassword() {
        return String.valueOf(password);
    }

    public String getUsername() {
        return username;
    }

    public String getBirth() {
        return birth;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }


}



