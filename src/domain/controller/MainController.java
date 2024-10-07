package domain.controller;

import domain.service.MainService;
import domain.service.MainServiceImp;

import java.util.Scanner;

public class MainController {
    private int menu;

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        MainService userService = new MainServiceImp();
        while (true) {
            try {
                printMenu();
                String input = scanner.nextLine().trim();
                menu = Integer.parseInt(input);
                if (menu == 1) {
                    userService.loginMenu();
                    break;
                } else if (menu == 2) {
                    userService.registerMemberMenu();
                    break;
                } else if (menu == 3) {
                    userService.exitMenu();
                    break;
                } else {
                    System.out.println("1-3 사이의 수를 입력하세요");
                }
            } catch (NumberFormatException e) {
                System.out.println("유효하지 않은 입력입니다. 1-3 사이의 수를 입력하세요.");
            }
        }
    }
    // 메뉴 프린트
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
