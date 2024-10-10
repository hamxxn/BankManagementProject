package domain.controller;

import domain.service.MainService;
import domain.service.MainServiceImp;

import java.util.Scanner;

public class MainController {

    private static final int LOGIN = 1;
    private static final int REGISTER = 2;
    private static final int EXIT = 3;

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        MainService mainService = new MainServiceImp();
        boolean isRunning = true;  // 프로그램이 계속 실행되도록 제어할 플래그

        while (isRunning) {  // 프로그램이 종료될 때까지 반복
            try {
                printMenu();
                String input = scanner.nextLine().trim();
                int userSelection = Integer.parseInt(input);

                switch (userSelection) {
                    case LOGIN:
                        mainService.loginMenu();
                        break;  // 로그인 후에도 다시 메뉴로 돌아감
                    case REGISTER:
                        mainService.registerMemberMenu();
                        break;  // 회원가입 후에도 다시 메뉴로 돌아감
                    case EXIT:
                        mainService.exitMenu();
                        isRunning = false;  // 프로그램 종료
                        break;
                    default:
                        System.out.println("1-3 사이의 수를 입력하세요.");
                }
            } catch (NumberFormatException e) {
                System.out.println("유효하지 않은 입력입니다. 1-3 사이의 수를 입력하세요.");
            }
        }
    }

    public void printMenu() {
        System.out.println("---------------------");
        System.out.println("   BankU");
        System.out.println("---------------------");
        System.out.println("1) 로그인");
        System.out.println("2) 회원가입");
        System.out.println("3) 종료");
        System.out.println("---------------------");
        System.out.println("메뉴 번호를 입력하세요!");
    }
}
