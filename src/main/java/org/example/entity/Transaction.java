package org.example.entity;

import java.time.LocalDate;

public class Transaction {
    private LocalDate date;         // 거래 날짜
    private String userId;          // 회원 ID
    private String name;            // 사용자 이름
    private String accountNum;      // 계좌 번호
    private String transactionDetail; // 입출금 내역 ("+금액" 또는 "-금액")
    private String memo;            // 메모
    private int balance;            // 잔액

    // 생성자
    public Transaction(LocalDate date, String userId, String name, String accountNum,
                       String transactionDetail, String memo, int balance) {
        if (!transactionDetail.matches("[+-]\\d+")) {
            throw new IllegalArgumentException("입출금 내역은 '+금액' 또는 '-금액' 형식이어야 합니다.");
        }

        this.date = date;
        this.userId = userId;
        this.name = name;
        this.accountNum = accountNum;
        this.transactionDetail = transactionDetail;
        this.memo = memo;
        this.balance = balance;
    }

    public LocalDate getDate() {
        return date;
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

    public String getTransactionDetail() {
        return transactionDetail;
    }

    public String getMemo() {
        return memo;
    }

    public int getBalance() {
        return balance;
    }
}
