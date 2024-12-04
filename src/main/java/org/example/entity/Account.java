package org.example.entity;

import org.example.repository.AccountServiceRepository;
import org.example.repository.UserServiceRepository;

import java.time.LocalDate;

public class Account {
    private String userId;
    private String name;
    private String accountNum;
    private String accountPw;
    private int balance;
    private String lastTransferDate;
    private String lastInterestDate;
    protected double interestRate;

    public Account(String userId, String name, String accountNum, String accountPw, int balance) {
        this.userId = userId;
        this.name = name;
        this.accountNum = accountNum;
        this.accountPw = accountPw;
        this.balance = balance;
        this.lastTransferDate = "1";
        interestRate=1.0;
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
    public void deposit(int amount) {
        balance += amount;
    }
    public void withdraw(int amount) {
        balance -= amount;
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

    public void giveInterest(LocalDate localDate) {
        // 마지막 이자 지급 날짜가 null 또는 빈 값인 경우 초기화
        if (lastInterestDate == null || lastInterestDate.isEmpty()) {
            System.out.println("이자 지급 기록이 없습니다. 초기화합니다.");
            lastInterestDate = localDate.toString();
            return;
        }

        // 마지막 이자 지급 날짜를 LocalDate로 변환
        LocalDate lastInterestLocalDate = LocalDate.parse(lastInterestDate);

        // 두 날짜 간 경과된 달 수 계산
        int yearDiff = localDate.getYear() - lastInterestLocalDate.getYear();
        int monthDiff = localDate.getMonthValue() - lastInterestLocalDate.getMonthValue();
        int totalMonths = (yearDiff * 12) + monthDiff;

        if (totalMonths >= 1) {
            System.out.println(totalMonths + "달이 경과하여 이자를 지급합니다.");

            // 이자 지급 로직
            for (int i = 0; i < totalMonths; i++) {
                balance += (balance * interestRate / 100); // 현재 잔액에 이자 추가
            }

            // 마지막 이자 지급 날짜 업데이트
            lastInterestDate = localDate.minusMonths(totalMonths - 1).toString();

            // 파일 업데이트
            AccountServiceRepository accountServiceRepository = new AccountServiceRepository();
            accountServiceRepository.save(this);
            accountServiceRepository.updateAccountFile("AccountInfo.txt");

            UserServiceRepository userServiceRepository = new UserServiceRepository();
            userServiceRepository.updateUserBalance(this);

            System.out.println("최종 잔액: " + balance);
        } else {
            System.out.println("1달 미만으로 경과했습니다. 이자 지급이 필요 없습니다.");
        }
    }
}
