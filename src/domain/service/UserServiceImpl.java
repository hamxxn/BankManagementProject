package domain.service;

import domain.entity.*;
import domain.repository.AccountServiceRepository;
import domain.repository.UserServiceRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
            if(accountNum-1>user.getAccounts().size()) {
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

            // 날짜 입력
            System.out.println("날짜를 입력해주세요. YYYY-MM-DD 형식입니다.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String dayInput = scanner.nextLine().trim();

            if (dayInput.equals("q")) { // q 입력시
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            if (dayInput.length() != 10) { // 입력된 값이 10자리가 아닐 경우
                System.out.println("잘못된 날짜 형식입니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");  // 날짜 형식 지정
            LocalDate date = LocalDate.parse(dayInput, formatter); // 날짜 파싱 및 유효성 확인

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

            userServiceRepository.updateUserFile("UserInfo.txt");
            accountServiceRepository.updateAccountFile("AccountInfo.txt");

        } catch (NumberFormatException e) {
            System.out.println("유효한 숫자가 아닙니다. 메뉴로 돌아갑니다.");
            return;
        } catch (DateTimeParseException e) {
            System.out.println("잘못된 날짜 형식입니다. 메뉴로 돌아갑니다.");
            return;
        } catch (Exception e) {
            System.out.println("잘못된 입력 형식입니다. 메뉴로 돌아갑니다.");
            return;
        }
    }

    //이체
    public void transfer() {
        if (user.getAccounts().isEmpty()) {
            System.out.println("계좌가 존재하지 않습니다.");
            System.out.println("메뉴로 돌아갑니다.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        Account sourceAccount = user.getAccounts().get(0);
        Account targetAccount = null;

        System.out.println("*** 계좌 이체 ***");
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
            if(accountNum-1>user.getAccounts().size()) {
                System.out.println("1-"+user.getAccounts().size()+" 사이의 수만 입력가능합니다. 메뉴로 돌아갑니다.");
                return;
            }
            Account account = user.getAccounts().get(accountNum-1);
            // 받는 계좌 입력
            System.out.println("이체할 계좌의 계좌번호를 입력해주세요.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String targetAccountNum = scanner.nextLine().trim();

            if (targetAccountNum.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            // 찾으려는 계좌가 있는지 확인
            targetAccount = accountServiceRepository.getAccountByAccountNum(targetAccountNum);
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

            // 날짜 입력
            System.out.println("날짜를 입력해주세요. YYYY-MM-DD 형식입니다.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String dayInput = scanner.nextLine().trim();

            if (dayInput.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            // 날짜 형식 지정
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dayInput, formatter);

            String lastTransferDate = sourceAccount.getLastTransferDate();
            if (lastTransferDate.equals(dayInput)) {
                System.out.println("1일 제한 횟수를 초과하여 계좌 이체에 실패하였습니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            sourceAccount.setLastTransferDate(dayInput);

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

            userServiceRepository.updateUserFile("UserInfo.txt");
            accountServiceRepository.updateAccountFile("AccountInfo.txt");

        } catch (NumberFormatException e) {
            System.out.println("금액은 숫자 형식으로 입력해야 합니다. 메뉴로 돌아갑니다.");
            return;
        } catch (DateTimeParseException e) {
            System.out.println("날짜는 YYYY-MM-DD 형식이어야 합니다. 메뉴로 돌아갑니다.");
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
            if(accountNum-1>user.getAccounts().size()) {
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

            // 날짜 입력
            System.out.println("날짜를 입력해주세요. YYYY-MM-DD 형식입니다.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String dayInput = scanner.nextLine().trim();

            if (dayInput.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            if (dayInput.length() != 10) {
                System.out.println("잘못된 날짜 형식입니다. 메뉴로 돌아갑니다.");
                return;
            }

            // 날짜 형식 지정 및 파싱
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dayInput, formatter);

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

            // 데이터 업데이트
            userServiceRepository.updateUserFile("UserInfo.txt");
            accountServiceRepository.updateAccountFile("AccountInfo.txt");

        } catch (NumberFormatException e) {
            System.out.println("숫자 형식이어야 합니다. 메뉴로 돌아갑니다.");
            return;
        } catch (DateTimeParseException e) {
            System.out.println("날짜는 YYYY-MM-DD 형식이어야 합니다. 메뉴로 돌아갑니다.");
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

        if (user.getAccountsCount() >= 3) {
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
                 account = new Account151(user.getUsername(), accountNum, password, 0);

                } else if (user.getAccounts().size()==1) {
                    account = new Account152(user.getUsername(), accountNum, password, 0);
                } else if (user.getAccounts().size()==2) {
                    account = new Account153(user.getUsername(), accountNum, password, 0);
                }

                user.addAccount(account);
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