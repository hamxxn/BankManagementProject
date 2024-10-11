package domain.service;

import domain.dto.AccountInfo;
import domain.entity.Account;
import domain.entity.User;

import java.util.Scanner;

public class UserServiceImp implements UserService {
    private User user;
    public UserServiceImp(User user) {
        this.user = user;
    }
    // 입금
    public void deposit(){};
    //이체
    public void transfer(){
    }
    //출금
    public void withdraw(){
        Scanner scanner = new Scanner(System.in);
        Account account = user.getAccounts().get(0);
        while (true) {
            System.out.println("출금할 금액을 입력해주세요: ");
            String input = scanner.nextLine().trim();

            if (input.equals("q")) {
                System.out.println("메뉴로 이동합니다.");
                break;
            }

            if (Integer.parseInt(input)<0 || Integer.parseInt(input)>account.getBalance()) {
                System.out.println("출금에 실패하였습니다. 메뉴로 이동합니다.");
                break;
            }

            System.out.println("날짜를 입력해주세요. (YYYY-MM-DD)");
            String birth = scanner.nextLine().trim();
            // 날짜 받아서 머하지?

            if (birth.equals("q")) {
                System.out.println("메뉴로 이동합니다.");
                break;
            }

            System.out.println("계좌 비밀번호를 입력해주세요.");
            String password = scanner.nextLine().trim();

            if (password.equals("q")) {
                System.out.println("메뉴로 이동합니다.");
                break;
            }

            if (!password.equals(account.getAccountPw())) {
                System.out.println("비밀번호가 일치하지 않습니다.");
                break;
            }

            account.setBalance(account.getBalance()-Integer.parseInt(input));
            System.out.println("출금에 성공하셨습니다!");
            System.out.println(account.getName()+"님의 현재 잔액은 "+ account.getBalance()+"원입니다.");

        }


    }
    
    public void createAccount() {
        if (user.getAccountsCount()>=1) {
            System.out.println(user.getUsername() + "님, 계좌를 개설하실 수 없습니다. 메뉴로 이동합니다.");
            return;
        }

        System.out.println(user.getUsername() + "님, 계좌 개설이 가능합니다!");

        Scanner scanner = new Scanner(System.in);
        String password;
        String passwordCheck;

        while (true) {
            System.out.println("계좌에 사용할 비밀번호 4자리를 입력해주세요. q 입력 시 메뉴로 이동합니다.");
            password = scanner.nextLine().trim();

            if (password.equals("q")) {
                System.out.println("메뉴로 이동합니다.");
                break;
            }

            // 비밀번호 길이 확인 및 숫자로만 구성되었는지 확인
            if (password.length() != 4 || !password.matches("\\d+")) {
                System.out.println("비밀번호는 4자리 숫자로만 구성되어야 합니다. 다시 입력해주세요.");
                continue; // 조건이 맞지 않으면 다시 입력받음
            }

            System.out.println("계좌에 사용할 비밀번호 4자리를 재입력해주세요. q 입력 시 메뉴로 이동합니다.");
            passwordCheck = scanner.nextLine().trim();

            if (passwordCheck.equals("q")) {
                System.out.println("메뉴로 이동합니다.");break;
            }

            // 비밀번호가 일치하는지 확인
            if (!password.equals(passwordCheck)) {
                System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
                continue;
            }

            // 모든 조건이 만족되면 계좌 생성
            //010 빼고 계좌생성
            String accountNum = "151" + user.getPhoneNum().substring(3);
            System.out.println(accountNum);

            Account account = new Account(user.getUsername() ,accountNum, password,0);
            user.addAccount(account);

            System.out.println("계좌 개설이 완료되었습니다.");
            System.out.println(user.getUsername() + "님의 계좌번호는 " + account.getAccountNum() + "입니다.");
            System.out.println("메인화면으로 돌아갑니다");
            break;
        }
    }

}
