package domain.service;

import domain.dto.AccountInfo;
import domain.entity.User;

import java.util.Scanner;

public class UserServiceImp implements UserService {
    private User user;
    public UserServiceImp(User user) {
        this.user = user;
    }
    public void deposit(){};

    public void transfer(){}

    public void withdraw(){}
    
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
            password = scanner.nextLine();

            if (password.equals("q")) {
                System.out.println("메뉴로 이동합니다.");
                return;
            }

            // 비밀번호 길이 확인 및 숫자로만 구성되었는지 확인
            if (password.length() != 4 || !password.matches("\\d+")) {
                System.out.println("비밀번호는 4자리 숫자로만 구성되어야 합니다. 다시 입력해주세요.");
                continue; // 조건이 맞지 않으면 다시 입력받음
            }

            System.out.println("계좌에 사용할 비밀번호 4자리를 재입력해주세요. q 입력 시 메뉴로 이동합니다.");
            passwordCheck = scanner.nextLine();

            if (passwordCheck.equals("q")) {
                System.out.println("메뉴로 이동합니다.");
                return;
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

            AccountInfo accountInfo = new AccountInfo(user.getUsername() ,accountNum, password,0);
            user.addAccount(accountInfo);

            System.out.println("계좌 개설이 완료되었습니다.");
            System.out.println(user.getUsername() + "님의 계좌번호는 " + accountInfo.getAccountNum() + "입니다.");
            System.out.println("메인화면으로 돌아갑니다");
            return;
        }
    }

}
