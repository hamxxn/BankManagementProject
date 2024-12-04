package org.example.entity;

import org.example.repository.AccountServiceRepository;
import org.example.repository.UserServiceRepository;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Account152 extends Account {
    //만기가 몇개월인지
    private int endAccount;
    public Account152(String userId, String name, String accountNum, String accountPw, int balance,String makeAccount,String accountType) {
        super(userId, name, accountNum, accountPw, balance,makeAccount,accountType);

    }

    private void printMenu() {
        System.out.println("1: 6개월 → 3%");
        System.out.println("2: 1년 → 4%");
        System.out.println("3: 2년 → 5%");
    }

    public void setInterestRate() {
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false; // 입력 유효성을 확인하는 변수

        while (!validInput) { // 유효한 입력이 들어올 때까지 반복
            try {
                // 메뉴 출력
                printMenu();
                System.out.print("원하는 옵션을 선택하세요 (1, 2, 3): ");
                int choice = scanner.nextInt();

                // 사용자의 선택에 따라 이자율 설정
                switch (choice) {
                    case 1:
                        interestRate = 3.0;
                        accountType="1";
                        endAccount = 6;
                        System.out.println("이자율이 3%로 설정되었습니다 (6개월)");
                        validInput = true; // 유효한 입력 처리
                        break;
                    case 2:
                        interestRate = 4.0;
                        accountType="2";
                        endAccount = 12;
                        System.out.println("이자율이 4%로 설정되었습니다 (1년)");
                        validInput = true; // 유효한 입력 처리
                        break;
                    case 3:
                        interestRate = 5.0;
                        accountType="3";
                        endAccount = 24;
                        System.out.println("이자율이 5%로 설정되었습니다 (2년)");
                        validInput = true; // 유효한 입력 처리
                        break;
                    default:
                        System.out.println("올바른 옵션을 선택하세요."); // 잘못된 입력 안내
                }
            } catch (InputMismatchException e) {
                System.out.println("숫자를 입력해야 합니다. 다시 시도하세요.");
                scanner.nextLine(); // 잘못된 입력 버퍼 비우기
            }
        }
    }

    public boolean EndAccount(LocalDate localDate) {
        if (makeAccount == null) {
            throw new IllegalStateException("계좌 생성 날짜가 설정되지 않았습니다.");
        }

        // makeAccount 날짜에서 endAccount 달을 더함
        LocalDate maturityDate = LocalDate.parse(makeAccount).plusMonths(endAccount);

        // localDate가 maturityDate 이후인지 확인
        return !localDate.isBefore(maturityDate);
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
            int monthsToPay = Math.min(totalMonths, endAccount);

            // 달 단위로 이자 계산 및 적용
            for (int i = 0; i < monthsToPay; i++) {
                balance += (balance * (interestRate / 100)); // 매달 이자를 현재 잔액에 추가
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
