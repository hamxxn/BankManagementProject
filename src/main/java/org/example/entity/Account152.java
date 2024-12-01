package org.example.entity;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Account152 extends Account {
    private int endAccount;
    private LocalDate makeAccount;
    public Account152(String userId, String name, String accountNum, String accountPw, int balance) {
        super(userId, name, accountNum, accountPw, balance);
        setInterestRate();
    }

    private void printMenu() {
        System.out.println("1: 6개월 → 3%");
        System.out.println("2: 1년 → 4%");
        System.out.println("3: 2년 → 5%");
    }

    private void setInterestRate() {
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
                        endAccount = 6;
                        System.out.println("이자율이 3%로 설정되었습니다 (6개월)");
                        validInput = true; // 유효한 입력 처리
                        break;
                    case 2:
                        interestRate = 4.0;
                        endAccount = 12;
                        System.out.println("이자율이 4%로 설정되었습니다 (1년)");
                        validInput = true; // 유효한 입력 처리
                        break;
                    case 3:
                        interestRate = 5.0;
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
    public void setMakeAccount(LocalDate makeAccount) {
        this.makeAccount = makeAccount;
    }
    public boolean EndAccount(LocalDate localDate) {
        if (makeAccount == null) {
            throw new IllegalStateException("계좌 생성 날짜가 설정되지 않았습니다.");
        }

        // makeAccount 날짜에서 endAccount 달을 더함
        LocalDate maturityDate = makeAccount.plusMonths(endAccount);

        // localDate가 maturityDate 이후인지 확인
        return !localDate.isBefore(maturityDate);
    }

}
