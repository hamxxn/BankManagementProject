package domain.controller;

import domain.entity.User;
import domain.service.UserService;
import domain.service.UserServiceImp;

import java.util.Scanner;

public class UserController {
    private int menu;
    private User user;
    public UserController(User user) {
        this.user = user;
    }
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserServiceImp(user);
        while (true) {
            System.out.println();
            try {
                printMenu();
                String input = scanner.nextLine();
                menu = Integer.parseInt(input);
                if (menu == 1) {
                    userService.deposit();
                    break;
                } else if (menu == 2) {
                    userService.withdraw();
                    break;
                } else if (menu == 3) {
                    userService.transfer();
                    break;
                } else if (menu == 4) {
                    userService.createAccount();
                    break;
                } else if (menu == 5) {
                    System.out.println("로그아웃 되었습니다.");
                    return;
                } else {
                    System.out.println("1-5 사이의 수를 입력하세요");
                }
            } catch (NumberFormatException e) {
                System.out.println("유효하지 않은 입력입니다. 1-5 사이의 수를 입력하세요.");
            }
        }
    }
    // 메뉴 프린트
    public void printMenu() {
        System.out.println("------입출금 기능------");
        System.out.println("1) 입금");
        System.out.println("2) 출금");
        System.out.println("3) 계좌 이체");
        System.out.println("4) 계좌 개설");
        System.out.println("5) 로그아웃");
        System.out.println("----------------------");
        System.out.println("메뉴 번호를 입력하세요!");
    }
}
