package domain.controller;

import domain.service.ManagerService;

import java.util.Scanner;

public class ManagerController {
    //Todo 사용자가 아이디로 admin 입력했을 때 Manager Controller로 넘어오도록 하기
    // MainServiceImpl 파일 loginMenu() 수정해야 함
    // 관리자 로그인할 때는 비밀번호가 없는 건가? 만약 그렇다면 admin이 아이디로 만들 수 없다고 안내를 하면 보안상 문제가 됨.

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    // 메뉴 선택 함수
    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                printMenu();
                String input = scanner.nextLine().trim();
                int userSelection = Integer.parseInt(input); // 사용자가 선택한 메뉴의 번호를 받는 변수
                if (userSelection == 1) {
                    managerService.accountSearch();
                    break;
                } else if (userSelection == 2) {
                    managerService.showAccountList();
                    break;
                } else if (userSelection == 3) {
                    managerService.logout();
                    break;
                } else {
                    System.out.println("1-3 사이의 수를 입력하세요");
                }
            } catch (NumberFormatException e) {
                System.out.println("유효하지 않은 입력입니다. 1-3 사이의 수를 입력하세요.");
            }
        }
    }

    // 메뉴 프린트 함수
    public void printMenu() {
        System.out.println("---------------------");
        System.out.println("   BankU");
        System.out.println("---------------------");
        System.out.println("1) 계좌 검색");
        System.out.println("2) 계좌 목록 조회");
        System.out.println("3) 로그아웃");
        System.out.println("---------------------");
        System.out.println("메뉴 번호를 입력하세요!");
    }
}
