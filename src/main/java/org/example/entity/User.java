package org.example.entity;

import java.time.LocalDate;
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
    public void removeAccount(Account account) {accounts.remove(account);}

    public List<Account> getAccounts() {
        return accounts;
    }

    public void changeUsername(String username) {
        this.username = username;
    }

    public void changePhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void printAccounts( ) {
        System.out.println("* "+username+"님의 계좌 리스트 *");
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println(i+1+". "+account.getName() +" "+account.getAccountNum()
                    +" "+ account.getBalance()+"원");
        }
    }
    public void printAccounts(String sub ) {
        System.out.println("* "+username+"님의 계좌 리스트 *");
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            if (accounts.get(i).getAccountNum().equals(sub)) {
                System.out.println(i+1+". "+account.getName() +" "+account.getAccountNum()
                        +" "+ account.getBalance()+"원 -> 현재 해약될 계좌이기에 선택 불가합니다.");
            }else {

                System.out.println(i + 1 + ". " + account.getName() + " " + account.getAccountNum()
                        + " " + account.getBalance() + "원");
            }
        }
    }

    public void giveInterest(LocalDate loginDate) {
        for(Account account : accounts){
            account.setInterestRate();
            account.giveInterest(loginDate);
        }
    }

    public boolean hasAccount151() {
        for(Account account:accounts){
            if(account.getAccountNum().substring(0,3).equals("151"))
                return true;
        }
        return false;
    }
}