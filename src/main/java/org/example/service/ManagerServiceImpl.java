package org.example.service;

import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.repository.AccountServiceRepository;
import org.example.repository.TransactionServiceRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ManagerServiceImpl implements ManagerService {
    AccountServiceRepository accountServiceRepository = new AccountServiceRepository();

    Scanner scanner = new Scanner(System.in);

    public void accountSearch() { // 계좌 검색 함수
        // 검색창에 이름 입력
        // q 입력 시, 관리자 메뉴로 돌아갑니다.
        // 이름 입력하면 <이름, 계좌번호, 잔액> 출력 (동명이인 존재 가능하므로 리스트로 출력할 것)
        // 이름이 현재 은행 기록에 존재한다면 계좌 정보를 출력한 후 관리자 메뉴로 돌아갑니다.
        // 만약, 이름이 존재하지 않는다면 해당 고객이 존재하지 않는다는 메시지를 출력한 후 관리자 메뉴로 돌아갑니다.
        while (true) {
            try {
                System.out.println("*** 계좌 검색 ***");
                System.out.println(" ");
                System.out.println("검색할 고객명을 입력해주세요. ");
                System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
                String searchName = scanner.nextLine().trim(); // 검색할 이름 입력

                if (searchName.equals("q")) { // q키 입력시 관리자 메뉴로 돌아감.
                    break;
                }
                List<Account> searchResult = accountServiceRepository.getAccountsByName(searchName);

                if (searchResult.isEmpty()) { // 해당 고객이 존재하지 않을 경우
                    System.out.println("해당 고객의 계좌는 존재하지 않습니다.");
                    System.out.println("메뉴로 돌아갑니다.");
                    break;
                }

                System.out.println("****************************************************");
                for (Account account : searchResult) {
                    System.out.println(account.getName() + ", " + account.getAccountNum() + ", " + account.getBalance());
                }
                System.out.println("****************************************************");
                break;
            } catch (Exception e) {
                System.out.println("잘못된 형식입니다.");
                System.out.println();
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }
        }
    }

    public void showAccountList() {
        // 계좌 목록 조회 함수
        // <이름, 계좌번호, 잔액> 출력
        List<Account> allAccounts = accountServiceRepository.getAccountsAll();
        System.out.println("*** 계좌 목록 조회 ***");
        System.out.println(" ");

        if (allAccounts.isEmpty()) { // 단 하나의 계좌도 존재하지 않을 경우
            System.out.println("현재 등록된 계좌가 없습니다.");
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

    @Override
    public void showAccountHistory(LocalDate todayDate) {

        if (accountServiceRepository.getAccountsAll().isEmpty()) {
            System.out.println("현재 등록된 계좌가 없습니다.");
            System.out.println("메뉴로 돌아갑니다.");
            return;
        }

        System.out.println("*** 기간별 계좌 조회 ***");

        for (int i = 0; i < accountServiceRepository.getAccountsAll().size(); i++) {
            Account account = accountServiceRepository.getAccountsAll().get(i);
            System.out.println(i+1+". "+account.getName() +" "+account.getAccountNum()
                    +" "+ account.getBalance()+"원");
        }

        System.out.println("조회할 계좌를 선택해주세요.");
        System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
        String in=scanner.nextLine().trim();

        if (in.equals("q")) {
            System.out.println("메뉴로 돌아갑니다.");
            return;
        }

        int accountNum= 0;
        try {
            accountNum = Integer.parseInt(in);
            if(accountNum > accountServiceRepository.getAccountsAll().size()) {
                System.out.println("유효한 범위의 입력이 아닙니다. 메뉴로 돌아갑니다.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("유효한 범위의 입력이 아닙니다. 메뉴로 돌아갑니다.");
            return;
        }
        Account account = accountServiceRepository.getAccountsAll().get(accountNum-1);


        System.out.println("조회를 희망하는 기간을 선택해주세요");
        System.out.println("1. 최근 1개월");
        System.out.println("2. 최근 3개월");
        System.out.println("3. 전체 기간");
        try{
        String Selectionperiod = scanner.nextLine().trim();

        LocalDate startDate = null;
        String period="";
        switch (Selectionperiod) {
            case "1":
                startDate = todayDate.minusMonths(1);
                period = "최근 1개월";
                break;
            case "2":
                startDate = todayDate.minusMonths(3);
                period = "최근 3개월";
                break;
            case "3":
                startDate = LocalDate.MIN; // 전체 기간
                period = "전체 기간";
                break;
            default:
                System.out.println("잘못된 입력입니다. 메뉴로 돌아갑니다.");
                return;
        }

        // 거래 내역 필터링 및 출력
        System.out.println("### " +period + " 내역 ###");
        LocalDate finalStartDate = startDate;
        TransactionServiceRepository transactionServiceRepository=new TransactionServiceRepository();
            List<Transaction> transactions = transactionServiceRepository.getTransactionList().stream()
                    .filter(t -> t.getAccountNum().equals(account.getAccountNum()) && !t.getDate().isBefore(finalStartDate))
                    .toList();


            if (transactions.isEmpty()) {
            System.out.println("선택하신 기간 내 거래 내역이 없습니다.");
        } else {
            System.out.println("<일시, 거래 유형, 메모, 거래 금액, 잔액>");
            for (Transaction transaction : transactions) {
                System.out.println(transaction.getDate()+"\t"+transaction.getTransactionType()+"\t"+transaction.getTransactionAmount()+"\t"+transaction.getMemo()+"\t"+transaction.getBalance());
            }
        }


        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력 형식입니다. 메뉴로 돌아갑니다.");
            return;
        } catch (Exception e) {
            System.out.println("잘못된 입력 형식입니다. 메뉴로 돌아갑니다.");
            return;
        }
    }
}
