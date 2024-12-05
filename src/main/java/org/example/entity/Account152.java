package org.example.entity;

import org.example.repository.AccountServiceRepository;
import org.example.repository.UserServiceRepository;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Account152 extends Account {
    //만기가 몇개월인지
    private int endAccount;
    public Account152(String userId, String name, String accountNum, String accountPw, int balance,String lastInterestDate, String makeAccount,String accountType) {
        super(userId, name, accountNum, accountPw, balance,lastInterestDate,makeAccount,accountType);
    }

    public void setEndAccount() {
        if (accountType.equals("1")){
            endAccount = 6;
        }else if (accountType.equals("2")){
            endAccount = 12;
        }else if (accountType.equals("3"))
            endAccount = 24;
    }

    public boolean EndAccount(LocalDate localDate) {
        if (makeAccount == null) {
            throw new IllegalStateException("계좌 생성 날짜가 설정되지 않았습니다.");
        }
        setEndAccount();
        // makeAccount 날짜에서 endAccount 달을 더함
        LocalDate maturityDate = LocalDate.parse(makeAccount).plusMonths(endAccount);

        // localDate가 maturityDate 이후인지 확인
        return !localDate.isBefore(maturityDate);
    }

    public void giveInterest(LocalDate loginDate) {
        // 계좌 만료일 확인: 만료일이 지나면 이자 지급 안 함
        if (EndAccount(loginDate)) {
            System.out.println("계좌 만료일이 지나 이자는 지급되지 않습니다.");
            return;
        }

        // 마지막 이자 지급 날짜가 null 또는 빈 값인 경우 초기화
        if (lastInterestDate == null || lastInterestDate.isEmpty()) {
            System.out.println("이자 지급 기록이 없습니다. 초기화합니다.");
            lastInterestDate = loginDate.toString();
            return;
        }
        setEndAccount();
        // 마지막 이자 지급 날짜를 LocalDate로 변환
        LocalDate lastInterestLocalDate = LocalDate.parse(lastInterestDate);



        // 두 날짜 간 경과된 달 수 계산
        int yearDiff = loginDate.getYear() - lastInterestLocalDate.getYear();
        int monthDiff = loginDate.getMonthValue() - lastInterestLocalDate.getMonthValue();
        int totalMonths = (yearDiff * 12) + monthDiff;


        if (totalMonths >= 1) {
            System.out.println(totalMonths + "달이 경과하여 이자를 지급합니다.");
            int monthsToPay = Math.min(totalMonths, endAccount);

            // 달 단위로 이자 계산 및 적용
            for (int i = 0; i < monthsToPay; i++) {
                balance += (balance * (interestRate / 100)); // 매달 이자를 현재 잔액에 추가
            }

            // 마지막 이자 지급 날짜 업데이트
            lastInterestDate = loginDate.minusMonths(totalMonths - 1).toString();

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
}
