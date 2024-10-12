package domain.service;

import domain.entity.Account;
import domain.repository.AccountServiceRepository;

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
                System.out.println("검색할 고객명을 입력해주세요. ");
                System.out.println("q 입력시 메뉴로 돌아갑니다.");
                String searchName = scanner.nextLine().trim(); // 검색할 이름 입력

                if (searchName.equals("q")) { // q키 입력시 관리자 메뉴로 돌아감.
                    break;
                }
                List<Account> searchResult = accountServiceRepository.getAccountsByName(searchName);

                if (searchResult.isEmpty()) { // 해당 고객이 존재하지 않을 경우
                    System.out.println("해당 고객은 존재하지 않습니다.");
                    System.out.println("메뉴로 돌아갑니다.");
                    break;
                }

                System.out.println("****************************************************");
                for (Account account : searchResult) {
                    System.out.println(account.getName() + ", " + account.getAccountNum() + ", " + account.getBalance());
                }
                System.out.println("****************************************************");

            } catch (Exception e) {
                System.out.println("잘못된 형식입니다.");
                System.out.println();
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }
        }
        return;
    }

    public void showAccountList() { // 계좌 목록 조회 함수
        // <이름, 계좌번호, 잔액> 출력
        List<Account> allAccounts = accountServiceRepository.getAccountsAll();
        System.out.println("*** 계좌 목록 조회 ***");

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
