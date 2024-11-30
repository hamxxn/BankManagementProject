package org.example.entity;

public class Account153 extends Account {
    public Account153(String userId, String name, String accountNum, String accountPw, int balance) {
        super(userId, name, accountNum, accountPw, balance);
    }
    private void setInterestRate(){
        super.interestRate = 1.0;
    }
}
