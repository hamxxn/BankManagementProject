package org.example.entity;

public class Account {
    private String userId;
    private String name;
    private String accountNum;
    private String accountPw;
    private int balance;
    private String lastTransferDate;
    private String lastInterestDate;

    public Account(String userId, String name, String accountNum, String accountPw, int balance) {
        this.userId = userId;
        this.name = name;
        this.accountNum = accountNum;
        this.accountPw = accountPw;
        this.balance = balance;
        this.lastTransferDate = "1";
        this.lastInterestDate = "1";
    }
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public String getAccountPw() {
        return accountPw;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getLastTransferDate() {
        return lastTransferDate;
    }
    public void setLastTransferDate(String lastTransferDate) {
        this.lastTransferDate = lastTransferDate;
    }

    public String getLastInterestDate() {
        return lastInterestDate;
    }
    public void setLastInterestDate(String lastInterestDate) {
        this.lastInterestDate = lastInterestDate;
    }

    public void changeUserName(String name) {
        this.name = name;
    }

}
