package org.example.service;

import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.repository.AccountServiceRepository;
import org.example.repository.TransactionServiceRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ManagerServiceImpl implements ManagerService {
    AccountServiceRepository accountServiceRepository = new AccountServiceRepository();

    Scanner scanner = new Scanner(System.in);

    public void accountSearch(String todayDate) { // 계좌 검색 함수
        while (true) {
            try {
                System.out.println("*** 계좌 검색 ***");
                System.out.println(" ");
                List<Account> allAccounts = accountServiceRepository.getAccountsAll();
                for (int i = 0; i < allAccounts.size(); i++) {
                    System.out.println((i + 1) + ". " + allAccounts.get(i).getName() + ", " + allAccounts.get(i).getAccountNum() + ", " + allAccounts.get(i).getBalance());
                }
                System.out.println("****************************************************");
                System.out.println("검색할 계좌의 번호를 입력해주세요. ");
                System.out.println("(q 입력시 메뉴로 돌아갑니다.)");

                String searchNumInString = scanner.nextLine().trim(); // 검색할 이름 입력
                if (searchNumInString.equalsIgnoreCase("q")) { // q키 입력시 관리자 메뉴로 돌아감.
                    break;
                }

                int searchNum;
                try {
                    searchNum = Integer.parseInt(searchNumInString);
                } catch (NumberFormatException e) {
                    System.out.println("숫자를 입력해주세요.");
                    continue;
                }

                if (searchNum < 1 || searchNum > allAccounts.size()) {
                    System.out.println("유효하지 않은 번호입니다. 다시 입력해주세요.");
                    continue;
                }

                Account searchAccount = accountServiceRepository.getAccountByAccountNum(allAccounts.get(searchNum - 1).getAccountNum());
                System.out.println("조회를 희망하는 기간을 선택해주세요");
                System.out.println("1. 최근 1개월");
                System.out.println("2. 최근 3개월");
                System.out.println("3. 전체 기간");
                String selectionPeriod = scanner.nextLine().trim();

                LocalDate startDate = null;
                String period = "";
                LocalDate today = LocalDate.parse(todayDate);
                switch (selectionPeriod) {
                    case "1":
                        startDate = today.minusMonths(1);
                        period = "최근 1개월";
                        break;
                    case "2":
                        startDate = today.minusMonths(3);
                        period = "최근 3개월";
                        break;
                    case "3":
                        startDate = LocalDate.MIN; // 전체 기간
                        period = "전체 기간";
                        break;
                    default:
                        System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
                        continue;
                }

                // 거래 내역 필터링 및 출력
                System.out.println("### " + period + " 내역 ###");
                LocalDate finalStartDate = startDate;
                TransactionServiceRepository transactionServiceRepository = new TransactionServiceRepository();
                List<Transaction> transactions = transactionServiceRepository.getTransactionList().stream()
                        .filter(t -> t.getAccountNum().equals(searchAccount.getAccountNum()) && !t.getDate().isBefore(finalStartDate))
                        .toList();

                if (transactions.isEmpty()) {
                    System.out.println("선택하신 기간 내 거래 내역이 없습니다.");
                } else {
                    System.out.println("<일시, 거래 유형, 메모, 거래 금액, 잔액>");
                    for (Transaction transaction : transactions) {
                        System.out.println(transaction.getDate() + "\t" + transaction.getTransactionType() + "\t" + transaction.getTransactionAmount() + "\t" + transaction.getMemo() + "\t" + transaction.getBalance());
                    }
                }
                break;

            } catch (Exception e) {
                System.out.println("오류가 발생했습니다. 다시 시도해주세요.");
            }
        }
    }


    public void showAccountList() { // 계좌 목록 조회 함수
        // <이름, 계좌번호, 잔액> 출력
        List<Account> allAccounts = accountServiceRepository.getAccountsAll();
        System.out.println("*** 계좌 목록 조회 ***");
        System.out.println(" ");

        if (allAccounts.isEmpty()) { // 단 하나의 계좌도 존재하지 않을 경우
            System.out.println("현재 존재하는 계좌가 없습니다.");
            System.out.println();
            System.out.println("메뉴로 돌아갑니다.");
            return;
        }

        System.out.println("********************* 계좌 목록 *********************");
        for (Account account : allAccounts) {
            System.out.println(account.getName() + ", " + account.getAccountNum() + ", " + account.getBalance());
        }
        System.out.println("****************************************************");

        return;
    }
}
