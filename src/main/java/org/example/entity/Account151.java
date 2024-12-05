package org.example.entity;

public class Account151 extends Account {
    public Account151(String userId, String name, String accountNum, String accountPw, int balance, String lastInterestDate,String makeAccount, String accountType) {
        super(userId, name, accountNum, accountPw, balance,lastInterestDate,makeAccount,accountType);
        setInterestRate();
    }
}
