package org.example.entity;

import java.time.LocalDate;

public class Transaction {
    private LocalDate date;         // 거래 날짜
    private String accountNum;   //계좌번호
    private String transactionType; //거래 유형
    private String memo;  // 메모
    private int transactionAmount; // 거래 금액
    private int balance;            // 잔액

    // 생성자
    public Transaction(LocalDate date, String accountNum,String transactionType,
                       String memo, int transactionAmount,int balance) {

        this.date = date;
//        this.userId = userId;
//        this.name = name;
        this.accountNum = accountNum;
        this.transactionType = transactionType;
        this.memo = memo;
        this.transactionAmount = transactionAmount;
        this.balance = balance;
    }

    public LocalDate getDate() {
        return date;
    }

//    public String getUserId() {
//        return userId;
//    }
//
//    public String getName() {
//        return name;
//    }

    public String getAccountNum() {
        return accountNum;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public int getTransactionAmount() {
        return transactionAmount;
    }

    public String getMemo() {
        return memo;
    }

    public int getBalance() {
        return balance;
    }
}
