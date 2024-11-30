package org.example.entity;

public class Account151 extends Account {

    public Account151(String userId, String name, String accountNum, String accountPw, int balance) {
        super(userId, name, accountNum, accountPw, balance);
        setInterestRate();
    }
    private void setInterestRate(){
        super.interestRate = 1.2;
    }
}
