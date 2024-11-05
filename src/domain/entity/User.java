package domain.entity;


import java.util.ArrayList;
import java.util.List;


public class User {
    private String id;
    private String username;
    private String password;
    private String birth;
    private String phoneNum;
    private List<Account> accounts;

    public User(String id, String password, String username, String phoneNum, String birth,ArrayList<Account> accounts) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.phoneNum= phoneNum;
        this.birth = birth;
        this.accounts = accounts;
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

    public int getAccountsCount() {
        return accounts.size();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void printAccounts() {
        System.out.println("* "+username+"님의 계좌 리스트 *");
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println(i+". "+account.getName() +" "+account.getAccountNum()
                    +" "+ account.getBalance()+"원");
        }
    }
}