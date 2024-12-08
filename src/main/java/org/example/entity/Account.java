package org.example.entity;

import org.example.repository.AccountServiceRepository;
import org.example.repository.UserServiceRepository;

import java.time.LocalDate;

public class Account {
    private String userId;
    private String name;
    private String accountNum;
    private String accountPw;
    protected int balance;
    private String lastTransferDate;
    protected String lastInterestDate;
    protected double interestRate;
    protected String makeAccount;
    protected String accountType;

    public Account(String userId, String name, String accountNum, String accountPw, int balance, String lastInterestDate,String makeAccount,String accountType) {
        this.userId = userId;
        this.name = name;
        this.accountNum = accountNum;
        this.accountPw = accountPw;
        this.balance = balance;
        this.lastTransferDate = "1";
        interestRate=1.0;
        this.makeAccount = makeAccount;
        this.accountType = accountType;
        this.lastInterestDate = lastInterestDate;
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

    public void setInterestRate(){
        switch (accountType){
            case "0":
                this.interestRate = 1.2;
            case "1":
                this.interestRate = 3.0;
//                this.endAccount = 6;
                break;
            case "2":
                this.interestRate = 4.0;
//                this.endAccount = 12;
                break;
            case "3":
                this.interestRate = 5.0;
//                this.endAccount = 24;
                break;
        }
    }
    public void giveInterest(LocalDate loginDate) {
        // 마지막 이자 지급 날짜가 null 또는 빈 값인 경우 초기화
        if (lastInterestDate == null || lastInterestDate.isEmpty()) {
            System.out.println("이자 지급 기록이 없습니다. 초기화합니다.");
            lastInterestDate = loginDate.toString();
            return;
        }

        // 마지막 이자 지급 날짜를 LocalDate로 변환
        LocalDate lastInterestLocalDate = LocalDate.parse(lastInterestDate);

        // 두 날짜 간 경과된 달 수 계산
        int yearDiff = loginDate.getYear() - lastInterestLocalDate.getYear();
        int monthDiff = loginDate.getMonthValue() - lastInterestLocalDate.getMonthValue();
        int totalMonths = (yearDiff * 12) + monthDiff;

        if (totalMonths >= 1) {
            System.out.println(totalMonths + "달이 경과하여 이자를 지급합니다.");

            // 이자 지급 로직
            for (int i = 0; i < totalMonths; i++) {
                balance += (balance * interestRate / 100); // 현재 잔액에 이자 추가
            }

            // 마지막 이자 지급 날짜 업데이트
            this.lastInterestDate = loginDate.minusMonths(totalMonths - 1).toString();

            // 파일 업데이트
            AccountServiceRepository accountServiceRepository = new AccountServiceRepository();
            accountServiceRepository.save(this);
            accountServiceRepository.updateAccountFile("AccountInfo.txt");

            UserServiceRepository userServiceRepository = new UserServiceRepository(accountServiceRepository);
            userServiceRepository.updateUserBalance(this);

            System.out.println("최종 잔액: " + balance);
        } else {
            System.out.println("1달 미만으로 경과했습니다. 이자 지급이 필요 없습니다.");
        }
    }

    public String getMakeDate() {
        return makeAccount;
    }

    public String getAccountType() {
        return accountType;
    }
}
