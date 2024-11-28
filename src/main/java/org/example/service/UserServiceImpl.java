package org.example.service;

import org.example.entity.*;
import org.example.repository.AccountServiceRepository;
import org.example.repository.UserServiceRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class UserServiceImpl implements UserService {
    private final User user;
    private final UserServiceRepository userServiceRepository;
    private final AccountServiceRepository accountServiceRepository;

    public UserServiceImpl(User user, UserServiceRepository userServiceRepository, AccountServiceRepository accountServiceRepository) {
        this.user = user;
        this.userServiceRepository = userServiceRepository;
        this.accountServiceRepository = accountServiceRepository;
    }

    public void myPage() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            printMyPageMenu();
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    // 이름 변경
                    changeUserName();
                    break;
                case "2":
                    // 전화번호 변경
                    changePhoneNumber();
                    break;
                case "3":
                    // 입출금 내역 조회
                    showAccountHistory();
                    break;
                case "q":
                    System.out.println("이전 메뉴로 돌아갑니다.");
                    return; // 돌아가기
                default:
                    System.out.println("유효하지 않은 입력입니다. 1-3 사이의 수를 입력하세요.");
            }
        }
    }

    public void printMyPageMenu() {
        System.out.println("---------------------");
        System.out.println("My Page Menu");
        System.out.println("---------------------");
        System.out.println("1) 이름 변경");
        System.out.println("2) 전화번호 변경");
        System.out.println("3) 입출금 내역 조회");
        System.out.println("---------------------");
        System.out.println("메뉴 번호를 입력하세요! (q: 돌아가기)");
    }

    // 이름 유효성 검증 (한글, 길이, 공백, admin 제외)
    private boolean isValidName(String name) {
        String koreanNamePattern = "^[가-힣]{2,5}$";
        if (name.equalsIgnoreCase("admin")) {
            return false;
        }
        return name.matches(koreanNamePattern);
    }

    // 사용자 이름 변경
    public void changeUserName(){
        System.out.println();
        System.out.println("*** 이름 변경 ***");
        System.out.println("새로운 이름을 입력해주세요. 한국어만 가능하며 길이는 2이상 5이하입니다. 중간에 공백이 포함되어서는 안됩니다.");
        Scanner scanner = new Scanner(System.in);
        String newName = scanner.nextLine().trim();

        if(newName.equals("q")){ // q 입력 시 로그인 후 메뉴로 돌아감.
            System.out.println("메뉴로 돌아갑니다.");
            return;
        }

        if(!isValidName(newName)){
            System.out.println("이름 형식에 맞지 않습니다. 메뉴로 돌아갑니다.");
            return;
        }

        user.changeUsername(newName); // user 정보 수정
        userServiceRepository.save(user);

        List<Account> targetAccountList = accountServiceRepository.getAccountsByUserId(user.getId()); // account 정보 수정
        for (Account account : targetAccountList) {
            account.changeUserName(newName);
            accountServiceRepository.save(account);
        }

        userServiceRepository.updateUserFile("UserInfo.txt");
        accountServiceRepository.updateAccountFile("AccountInfo.txt");

        System.out.println("새로운 이름 <"+newName+">으로 변경되었습니다.");
    }

    // 전화번호 유효성 검증 (숫자 11자리인지 확인)
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{11}");
    }

    // 전화번호 변경
    public void changePhoneNumber(){
        System.out.println();
        System.out.println("*** 전화번호 변경 ***");
        System.out.println("새로운 전화번호를 입력해주세요. 숫자 11자리 형식만 가능합니다."); //Todo 전화번호 입력 예외 처리 해야 함
        Scanner scanner = new Scanner(System.in);
        String newPhoneNum = scanner.nextLine().trim();

        if(newPhoneNum.equals("q")){ // q 입력 시 로그인 후 메뉴로 돌아감.
            System.out.println("메뉴로 돌아갑니다.");
            return;
        }

        if(!isValidPhoneNumber(newPhoneNum)){
            System.out.println("전화번호 형식에 맞지 않습니다. 메뉴로 돌아갑니다.");
            return;
        }

        user.changePhoneNum(newPhoneNum); // user 정보 수정
        userServiceRepository.save(user);
        userServiceRepository.updateUserFile("UserInfo.txt");

        System.out.println("새로운 전화번호 <"+newPhoneNum+">으로 변경되었습니다.");
    }

    // 입출금 내역 조회
    public void showAccountHistory(){
        return;
    }

    // 입금
    public void deposit() {
        if (user.getAccounts().isEmpty()) {
            System.out.println("계좌가 존재하지 않습니다.");
            System.out.println("메뉴로 돌아갑니다.");
            return;
        }

        System.out.println("*** 입금 ***");
        System.out.println(" ");
        Scanner scanner = new Scanner(System.in);

        try {
            user.printAccounts();
            System.out.println("계좌를 선택해주세요.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String in=scanner.nextLine().trim();

            if (in.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }
            int accountNum=Integer.parseInt(in);
            if(accountNum>user.getAccounts().size()) {
                System.out.println("1-"+user.getAccounts().size()+" 사이의 수만 입력가능합니다. 메뉴로 돌아갑니다.");
                return;
            }
            Account account = user.getAccounts().get(accountNum-1);

            // 입금 금액 입력
            int depositAmount = 0;
            System.out.println("입금하실 금액을 입력해주세요. 숫자만 입력 가능합니다.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String depositAmountInput = scanner.nextLine().trim();

            if (depositAmountInput.equals("q")) { // q 입력시
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            depositAmount = Integer.parseInt(depositAmountInput); // 입력을 정수로 변환

            if (depositAmount <= 0) { // 양수 입력이 아닐 경우 return
                System.out.println("유효한 숫자가 아닙니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            // Todo 메모 입력

            // 계좌 비밀번호 입력
            System.out.println("계좌 비밀번호 4자리를 입력해주세요.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String password = scanner.nextLine().trim();

            if (password.equals("q")) { // q 입력했을 시
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            // 비밀번호가 숫자로만 구성된지 확인
            if (!password.matches("\\d{4}")) {
                System.out.println("비밀번호는 4자리 숫자여야 합니다.");
                return;
            }

            if (!password.equals(account.getAccountPw())) { // 비밀번호 틀렸을 시
                System.out.println("비밀번호가 일치하지 않습니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            // 최종 출력
            System.out.println("입력하신 금액: " + depositAmount + "원");
            account.setBalance(account.getBalance() + depositAmount); // 계좌에 입금 적용
            System.out.println("입금이 완료되었습니다.");
            System.out.println(account.getName() + "님의 현재 잔액은 " + account.getBalance() + "원입니다.");

            userServiceRepository.save(user);
            accountServiceRepository.save(account);

            userServiceRepository.updateUserFile("UserInfo.txt");
            accountServiceRepository.updateAccountFile("AccountInfo.txt");

        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력 형식입니다. 메뉴로 돌아갑니다.");
            return;
        } catch (DateTimeParseException e) {
            System.out.println("잘못된 날짜 형식입니다. 메뉴로 돌아갑니다.");
            return;
        } catch (Exception e) {
            System.out.println("잘못된 입력 형식입니다. 메뉴로 돌아갑니다.");
            return;
        }
    }

    //출금
    public void withdraw() {
        if (user.getAccounts().isEmpty()) {
            System.out.println("계좌가 존재하지 않습니다.");
            System.out.println("메뉴로 돌아갑니다.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("*** 출금 ***");
        System.out.println(" ");

        try {
            user.printAccounts();
            System.out.println("계좌를 선택해주세요.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String in=scanner.nextLine().trim();

            if (in.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }
            int accountNum=Integer.parseInt(in);
            if(accountNum>user.getAccounts().size()) {
                System.out.println("1-"+user.getAccounts().size()+" 사이의 수만 입력가능합니다. 메뉴로 돌아갑니다.");
                return;
            }
            Account account = user.getAccounts().get(accountNum-1);
            // 출금 금액 입력
            System.out.println("출금하실 금액을 입력해주세요. 숫자 형식입니다.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String input = scanner.nextLine().trim();

            if (input.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            int withdrawInput = Integer.parseInt(input);

            if (withdrawInput <= 0 || withdrawInput > account.getBalance()) {
                System.out.println("유효한 금액이 아닙니다. 메뉴로 돌아갑니다.");
                return;
            }

            // Todo 메모 입력

            // 비밀번호 입력 확인
            System.out.println("계좌 비밀번호 4자리를 입력해주세요.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String password = scanner.nextLine().trim();

            if (password.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            if (!password.equals(account.getAccountPw())) {
                System.out.println("비밀번호가 일치하지 않습니다. 메뉴로 돌아갑니다.");
                return;
            }

            // 출금 처리
            account.setBalance(account.getBalance() - withdrawInput);
            System.out.println("출금에 성공하셨습니다!");
            System.out.println(account.getName() + "님의 현재 잔액은 " + account.getBalance() + "원입니다.");

            userServiceRepository.save(user);
            accountServiceRepository.save(account);

            // 데이터 업데이트
            userServiceRepository.updateUserFile("UserInfo.txt");
            accountServiceRepository.updateAccountFile("AccountInfo.txt");

        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력 형식입니다. 메뉴로 돌아갑니다.");
            return;
        } catch (DateTimeParseException e) {
            System.out.println("잘못된 날짜 형식입니다. 메뉴로 돌아갑니다.");
            return;
        } catch (Exception e) {
            System.out.println("잘못된 입력 형식입니다. 메뉴로 돌아갑니다.");
            return;
        }
    }

    // 계좌 이체
    public void transfer(LocalDate todayDate) {
        if (user.getAccounts().isEmpty()) {
            System.out.println("계좌가 존재하지 않습니다.");
            System.out.println("메뉴로 돌아갑니다.");
            return;
        }

        Scanner scanner = new Scanner(System.in);


        System.out.println("*** 계좌 이체 ***");
        System.out.println(" ");


        try {
            user.printAccounts();
            //보내는 계좌 입력
            System.out.println("계좌를 선택해주세요.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String in=scanner.nextLine().trim();

            if (in.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }
            int accountNum=Integer.parseInt(in);
            if(accountNum>user.getAccounts().size()) {
                System.out.println("1-"+user.getAccounts().size()+" 사이의 수만 입력가능합니다. 메뉴로 돌아갑니다.");
                return;
            }
            Account sourceAccount = user.getAccounts().get(accountNum-1);
            // 받는 계좌 입력
            System.out.println("이체할 계좌의 계좌번호를 입력해주세요.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String targetAccountNum = scanner.nextLine().trim();

            if (targetAccountNum.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            } else if (targetAccountNum.equals(sourceAccount.getAccountNum())) {
                System.out.println("출금 계좌와 입금 계좌가 동일합니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }


            // 찾으려는 계좌가 있는지 확인
            User targetUser;
            targetUser = userServiceRepository.getUserByAccount(targetAccountNum);

            if (targetUser == null) {
                System.out.println("입력하신 계좌는 존재하지 않습니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            // 해당 사용자에서 계좌 찾기
            Account targetAccount = targetUser.getAccounts().stream()
                    .filter(account -> account.getAccountNum().equals(targetAccountNum))
                    .findFirst()
                    .orElse(null);

            if (targetAccount == null) {
                System.out.println("입력하신 계좌는 존재하지 않습니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            // 이체할 금액 입력
            System.out.println("이체하실 금액을 입력해주세요. 숫자만 입력 가능합니다.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String transferInput = scanner.nextLine().trim();

            if (transferInput.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            int transferAmount = Integer.parseInt(transferInput);
            if (transferAmount <= 0 || sourceAccount.getBalance() < transferAmount) {
                System.out.println("유효한 금액이 아니므로 계좌 이체에 실패하였습니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            String lastTransferDate = sourceAccount.getLastTransferDate();
            if (lastTransferDate.equals(todayDate.toString())) {
                System.out.println("1일 제한 횟수를 초과하여 계좌 이체에 실패하였습니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            sourceAccount.setLastTransferDate(todayDate.toString());

            // Todo 메모 입력 -------------

            // 비밀번호 체크
            System.out.println("계좌 비밀번호 4자리를 입력해주세요.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String password = scanner.nextLine().trim();

            if (password.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            if (!password.equals(sourceAccount.getAccountPw())) {
                System.out.println("비밀번호가 일치하지 않습니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            // 이체 처리
            sourceAccount.setBalance(sourceAccount.getBalance() - transferAmount);
            targetAccount.setBalance(targetAccount.getBalance() + transferAmount);

            System.out.println("계좌 이체에 성공하셨습니다!");
            System.out.println(sourceAccount.getName() + "님의 현재 잔액은 " + sourceAccount.getBalance() + "원입니다.");

            // targetUser의 accounts에서 targetAccount를 갱신
            targetUser.getAccounts().stream()
                    .filter(account -> account.getAccountNum().equals(targetAccount.getAccountNum()))
                    .forEach(account -> {
                        account.setBalance(targetAccount.getBalance()); // targetAccount의 새로운 잔액으로 갱신
                    });

            accountServiceRepository.save(sourceAccount);
            accountServiceRepository.save(targetAccount);

            if (user.getId().equals(targetUser.getId())) {
                // source 계좌와 target 계좌 모두 동일한 사용자에게 속할 경우
                Account newtargetAccount = null;
                for (Account a : user.getAccounts()) {
                    if (a.getAccountNum().equals(targetAccountNum)) {
                        newtargetAccount = a;
                    }
                }
                if (newtargetAccount!=null) {
                    newtargetAccount.setBalance(targetAccount.getBalance());
                }
                // 동일한 사용자이므로, user를 한 번만 저장함.
                userServiceRepository.save(user);
            } else {
                // 만약 user와 targetUser가 다를 경우, 각각 다른 계좌에 대해 저장
                userServiceRepository.save(user); // user의 계좌 업데이트
                userServiceRepository.save(targetUser); // targetUser의 계좌 업데이트
            }

            userServiceRepository.updateUserFile("UserInfo.txt");
            accountServiceRepository.updateAccountFile("AccountInfo.txt");

        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력 형식입니다. 메뉴로 돌아갑니다.");
            return;
        } catch (DateTimeParseException e) {
            System.out.println("잘못된 날짜 형식입니다. 메뉴로 돌아갑니다.");
            return;
        } catch (Exception e) {
            System.out.println("잘못된 입력 형식입니다. 메뉴로 돌아갑니다.");
            return;
        }
    }

    // 계좌 개설
    public void createAccount() {
        System.out.println("*** 계좌 개설 ***");
        System.out.println(" ");

        int totalacc = accountServiceRepository.getAccountsAll().size();

        if (totalacc >= 100000000 || user.getAccountsCount() >= 100000000) {
            System.out.println(user.getUsername() + "님, 계좌를 개설하실 수 없습니다.");
            System.out.println();
            System.out.println("메뉴로 이동합니다.");
            return;
        }

        System.out.println(user.getUsername() + "님, 계좌 개설이 가능합니다!");

        Scanner scanner = new Scanner(System.in);
        String password;
        String passwordCheck;

        try {
            while (true) {
                System.out.println();
                System.out.println("계좌에 사용할 비밀번호 4자리를 입력해주세요.");
                System.out.println("(q 입력 시 메뉴로 돌아갑니다.)");
                password = scanner.nextLine().trim();

                if (password.equals("q")) {
                    System.out.println("메뉴로 이동합니다.");
                    return;  // q 입력 시 즉시 종료
                }

                // 비밀번호 길이 확인 및 숫자로만 구성되었는지 확인
                if (password.length() != 4 || !password.matches("\\d+")) {
                    System.out.println("비밀번호는 4자리 숫자로만 구성되어야 합니다. 다시 입력해주세요.");
                    continue; // 조건이 맞지 않으면 다시 입력받음
                }

                System.out.println("계좌에 사용할 비밀번호 4자리를 재입력해주세요.");
                System.out.println("(q 입력 시 메뉴로 돌아갑니다.)");
                passwordCheck = scanner.nextLine().trim();

                if (passwordCheck.equals("q")) {
                    System.out.println("메뉴로 이동합니다.");
                    return;  // q 입력 시 즉시 종료
                }

                // 비밀번호가 일치하는지 확인
                if (!password.equals(passwordCheck)) {
                    System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
                    continue;
                }

                // 모든 조건이 만족되면 계좌 생성
                //010 빼고 계좌생성
                String accountNum="";
                if(user.getAccounts().size()==0){
                    accountNum = "151" + user.getPhoneNum().substring(3);

                } else if (user.getAccounts().size()==1) {
                    accountNum = "152" + user.getPhoneNum().substring(3);

                } else if (user.getAccounts().size()==2) {
                    accountNum = "153" + user.getPhoneNum().substring(3);
                }

                System.out.println(accountNum);
                Account account =null;
                if(user.getAccounts().size()==0){
                    account = new Account151(user.getId(), user.getUsername(), accountNum, password, 0);
                } else if (user.getAccounts().size()==1) {
                    account = new Account152(user.getId(), user.getUsername(), accountNum, password, 0);
                } else if (user.getAccounts().size()==2) {
                    account = new Account153(user.getId(), user.getUsername(), accountNum, password, 0);
                }

                user.addAccount(account);
                userServiceRepository.save(user);

                userServiceRepository.updateUserFile("UserInfo.txt");
                accountServiceRepository.addAccount(account);

                System.out.println("계좌 개설이 완료되었습니다.");
                System.out.println(user.getUsername() + "님의 계좌번호는 " + account.getAccountNum() + "입니다.");

                System.out.println();
                System.out.println("메인화면으로 돌아갑니다");
                break; // 모든 작업이 완료되면 반복 종료
            }
        } catch (Exception e) {
            System.out.println("잘못된 입력 형식입니다. 메뉴로 돌아갑니다.");
            return;
        }
    }
}