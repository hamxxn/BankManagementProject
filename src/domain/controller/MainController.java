package domain.controller;

import domain.service.MainService;
import domain.service.MainServiceImp;

import java.util.Scanner;

public class MainController {
    //Todo managerController처럼 얘도 로컬변수로 바꾸기
    private int userSelection; // 사용자가 선택한 메뉴의 번호를 받는 변수

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        MainService mainService = new MainServiceImp(); //Todo ManagerController처럼 final 변수로 선언할까?
        while (true) {
            try {
                printMenu();
                String input = scanner.nextLine().trim();
                userSelection = Integer.parseInt(input);
                if (userSelection == 1) {
                    mainService.loginMenu();
                    break;
                } else if (userSelection == 2) {
                    mainService.registerMemberMenu();
                    break;
                } else if (userSelection == 3) {
                    mainService.exitMenu();
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
