package org.example.service;

import org.example.entity.*;
import org.example.repository.AccountServiceRepository;
import org.example.repository.TransactionServiceRepository;
import org.example.repository.UserServiceRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class UserServiceImpl implements UserService {
    private final User user;
    private final UserServiceRepository userServiceRepository;
    private final AccountServiceRepository accountServiceRepository;
    private final TransactionServiceRepository transactionServiceRepository;

    public UserServiceImpl(User user, UserServiceRepository userServiceRepository, AccountServiceRepository accountServiceRepository,TransactionServiceRepository transactionServiceRepository) {
        this.user = user;
        this.userServiceRepository = userServiceRepository;
        this.accountServiceRepository = accountServiceRepository;
        this.transactionServiceRepository = transactionServiceRepository;
    }

    public void myPage(LocalDate todayDate) {
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
                    showAccountHistory(todayDate);
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
        System.out.println("새로운 전화번호를 입력해주세요. 숫자 11자리 형식만 가능합니다.");
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
    public void showAccountHistory(LocalDate todayDate){
        try {
            if (user.getAccounts().isEmpty()) {
                System.out.println("계좌가 존재하지 않습니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            System.out.println("*** 입출금 내역 조회 ***");
            System.out.println(" ");
            Scanner scanner = new Scanner(System.in);

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


            //기간 선택
            System.out.println("조회를 희망하는 기간을 선택해주세요");
            System.out.println("1. 최근 1개월");
            System.out.println("2. 최근 3개월");
            System.out.println("3. 전체 기간");
            String Selectionperiod = scanner.nextLine().trim();

            LocalDate startDate = null;
            String period="";
            switch (Selectionperiod) {
                case "1":
                    startDate = todayDate.minusMonths(1);
                    period = "최근 1개월";
                    break;
                case "2":
                    startDate = todayDate.minusMonths(3);
                    period = "최근 3개월";
                    break;
                case "3":
                    startDate = LocalDate.MIN; // 전체 기간
                    period = "전체 기간";
                    break;
                default:
                    System.out.println("잘못된 입력입니다. 메뉴로 돌아갑니다.");
                    return;
            }

            // 거래 내역 필터링 및 출력
            System.out.println("### " +period + " 내역 ###");
            LocalDate finalStartDate = startDate;
            List<Transaction> transactions = transactionServiceRepository.getTransactionList().stream()
                    .filter(t -> t.getAccountNum().equals(account.getAccountNum()) && !t.getDate().isBefore(finalStartDate))
                    .toList();

            if (transactions.isEmpty()) {
                System.out.println("선택하신 기간 내 거래 내역이 없습니다.");
            } else {
                System.out.println("<일시, 거래 유형, 메모, 거래 금액, 잔액>");
                for (Transaction transaction : transactions) {
                    System.out.println(transaction.getDate()+"\t"+transaction.getTransactionType()+"\t"+transaction.getTransactionAmount()+"\t"+transaction.getMemo()+"\t"+transaction.getBalance());
                }
            }


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

    // 입금
    public void deposit(LocalDate todayDate) {
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

            // 메모 입력
            System.out.println("입금 메모를 입력해주세요. 글자수는 최대 10자입니다.");
            System.out.println("공백은 반영되지 않으며, 메모를 원치 않으신다면 엔터를 눌러주세요.");

            String memoInput = scanner.nextLine();  // 사용자 입력
            memoInput = memoInput.replaceAll("\\s+", "");  // 모든 공백 제거
            if(memoInput.isEmpty())
                memoInput="메모없음";
            String memo = memoInput.length() > 10 ? memoInput.substring(0, 10) : memoInput;  // 최대 10글자로 자르기

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
            account.deposit(depositAmount);
            System.out.println("입금이 완료되었습니다.");
            System.out.println(account.getName() + "님의 현재 잔액은 " + account.getBalance() + "원입니다.");

            userServiceRepository.save(user);
            accountServiceRepository.save(account);

            userServiceRepository.updateUserFile("UserInfo.txt");
            accountServiceRepository.updateAccountFile("AccountInfo.txt");

            Transaction transaction = new Transaction(todayDate,account.getAccountNum(),"입금",memo,depositAmount,account.getBalance());
            transactionServiceRepository.add(transaction);

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
    public void withdraw(LocalDate todayDate) {
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
            String in = scanner.nextLine().trim();

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

            // 메모 입력
            System.out.println("출금 메모를 입력해주세요. 글자수는 최대 10자입니다.");
            System.out.println("공백은 반영되지 않으며, 메모를 원치 않으신다면 엔터를 눌러주세요.");

            String memoInput = scanner.nextLine();  // 사용자 입력
            memoInput = memoInput.replaceAll("\\s+", "");  // 모든 공백 제거
            if(memoInput.isEmpty())
                memoInput="메모없음";
            String memo = memoInput.length() > 10 ? memoInput.substring(0, 10) : memoInput;  // 최대 10글자로 자르기

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
            account.withdraw(withdrawInput);
            System.out.println("출금에 성공하셨습니다!");
            System.out.println(account.getName() + "님의 현재 잔액은 " + account.getBalance() + "원입니다.");
            if (account instanceof Account152) {
                boolean isMatured = ((Account152) account).EndAccount(todayDate);
                if(isMatured)
                    System.out.println("축하합니다. 적금 통장의 적금 만기일이 지났습니다!");
                else
                    System.out.println(account.getName()+"님의 적금 통장("+account.getAccountNum()+")은 자동해약 되셨습니다.");
                if (account.getBalance() > 0) {
                    System.out.println("잔액 " + account.getBalance() + "원을 송금하실 계좌를 아래에서 선택해주세요.");
                    user.printAccounts(account.getAccountNum());

                    while (true) {
                        System.out.println("송금받을 계좌를 선택해주세요.");
                        String transferAccountInput = scanner.nextLine().trim();

                        try {
                            int accountNumChoice = Integer.parseInt(transferAccountInput);

                            // 계좌 선택이 유효한지 확인
                            if (accountNumChoice > user.getAccounts().size()|| accountNumChoice <= 0) {
                                System.out.println("올바르지 않은 계좌 선택입니다. 다시 시도해주세요.");
                                continue; // 루프를 다시 시작
                            }

                            Account targetAccount = user.getAccounts().get(accountNumChoice - 1);
                            int remainingBalance = account.getBalance();

                            // 송금 처리
                            account.withdraw(remainingBalance);
                            targetAccount.deposit(remainingBalance);

                            System.out.println("남은 금액 " + remainingBalance + "원이 " + targetAccount.getAccountNum() + " 계좌로 송금되었습니다.");
                            break; // 성공적으로 처리되면 루프 종료
                        } catch (NumberFormatException e) {
                            System.out.println("올바르지 않은 형식입니다. 다시 시도해주세요.");
                        }
                    }
                }

                System.out.println(account.getName()+"님의 계좌("+account.getAccountNum()+")가 없어집니다.");

                accountServiceRepository.removeAccount(account.getAccountNum());
                user.removeAccount(account);

            }else{
                accountServiceRepository.save(account);
                Transaction transaction = new Transaction(todayDate,account.getAccountNum(),"출금",memoInput,withdrawInput,account.getBalance());
                transactionServiceRepository.add(transaction);
            }
            userServiceRepository.save(user);
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
            // 보내는 계좌 입력
            System.out.println("계좌를 선택해주세요.");
            System.out.println("(q 입력시 메뉴로 돌아갑니다.)");
            String in = scanner.nextLine().trim();

            if (in.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            int accountNum = Integer.parseInt(in);
            if (accountNum > user.getAccounts().size() || accountNum <= 0) {
                System.out.println("1-" + user.getAccounts().size() + " 사이의 수만 입력가능합니다. 메뉴로 돌아갑니다.");
                return;
            }

            Account sourceAccount = user.getAccounts().get(accountNum - 1);

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
            User targetUser = userServiceRepository.getUserByAccount(targetAccountNum);

            if (targetUser == null) {
                System.out.println("입력하신 계좌는 존재하지 않습니다.");
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

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

            // 메모 입력
            System.out.println("계좌이체 메모를 입력해주세요. 글자수는 최대 10자입니다.");
            System.out.println("공백은 반영되지 않으며, 메모를 원치 않으신다면 엔터를 눌러주세요.");

            String memoInput = scanner.nextLine();  // 사용자 입력
            memoInput = memoInput.replaceAll("\\s+", "");  // 모든 공백 제거
            if(memoInput.isEmpty())
                memoInput="메모없음";
            String memo = memoInput.length() > 10 ? memoInput.substring(0, 10) : memoInput;  // 최대 10글자로 자르기

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
            sourceAccount.withdraw(transferAmount);
            targetAccount.deposit(transferAmount);
            System.out.println("계좌 이체에 성공하셨습니다!");
            System.out.println(sourceAccount.getName() + "님의 현재 잔액은 " + sourceAccount.getBalance() + "원입니다.");
            sourceAccount.setLastTransferDate(todayDate.toString());

            // Account152 체크 및 처리
            if (sourceAccount instanceof Account152) {
                boolean isMatured = ((Account152) sourceAccount).EndAccount(todayDate);
                if (isMatured) {
                    System.out.println("축하합니다. 적금 통장의 적금 만기일이 지났습니다!");
                } else {
                    System.out.println(sourceAccount.getName() + "님의 적금 통장(" + sourceAccount.getAccountNum() + ")이 자동해약 되셨습니다.");
                }

                // 잔액 송금
                if (sourceAccount.getBalance() > 0) {
                    System.out.println("잔액 " + sourceAccount.getBalance() + "원을 송금하실 계좌를 아래에서 선택해주세요.");
                    user.printAccounts(sourceAccount.getAccountNum());

                    while (true) {
                        System.out.println("송금받을 계좌를 선택해주세요.");
                        String transferAccountInput = scanner.nextLine().trim();

                        try {
                            int transferAccountNum = Integer.parseInt(transferAccountInput);

                            if (transferAccountNum > user.getAccounts().size() || transferAccountNum == accountNum || transferAccountNum <= 0) {
                                System.out.println("올바르지 않은 계좌 선택입니다. 다시 시도해주세요.");
                                continue;
                            }

                            Account additionalTargetAccount = user.getAccounts().get(transferAccountNum - 1);
                            int remainingBalance = sourceAccount.getBalance();

                            // 송금 처리
                            sourceAccount.withdraw(remainingBalance);
                            additionalTargetAccount.deposit(remainingBalance);

                            System.out.println("남은 금액 " + remainingBalance + "원이 " + additionalTargetAccount.getAccountNum() + " 계좌로 송금되었습니다.");
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("올바르지 않은 형식입니다. 다시 시도해주세요.");
                        }
                    }
                }

                System.out.println(sourceAccount.getName() + "님의 계좌(" + sourceAccount.getAccountNum() + ")가 없어집니다.");
                user.removeAccount(sourceAccount);
                userServiceRepository.save(user);
                accountServiceRepository.removeAccount(sourceAccount.getAccountNum());
            } else {
                userServiceRepository.save(user);
                accountServiceRepository.save(sourceAccount);
                Transaction transaction1 = new Transaction(todayDate,sourceAccount.getAccountNum(),"출금",memo,transferAmount,sourceAccount.getBalance());
                transactionServiceRepository.add(transaction1);
                Transaction transaction2 = new Transaction(todayDate,targetAccount.getAccountNum(),"입금",memo,transferAmount,targetAccount.getBalance());
                transactionServiceRepository.add(transaction2);
            }

            accountServiceRepository.save(targetAccount);

            if (user.getId().equals(targetUser.getId())) {
                Account newTargetAccount = null;
                for (Account a : user.getAccounts()) {
                    if (a.getAccountNum().equals(targetAccountNum)) {
                        newTargetAccount = a;
                    }
                }
                if (newTargetAccount != null) {
                    newTargetAccount.setBalance(targetAccount.getBalance());
                }
                userServiceRepository.save(user);
            } else {
                userServiceRepository.save(user);
                userServiceRepository.save(targetUser);
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
    public void createAccount(LocalDate todayDate) {
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
                // 계좌 유형 선택
                int accountType = 0;
                while (true) {
                    System.out.println("-------------------------------");
                    System.out.println("계좌 유형을 선택해주세요:");
                    System.out.println("1. 자유입출금 계좌");
                    System.out.println("2. 정기 적금 계좌");
                    System.out.println("3. 모임통장 (아직 사용할 수 없는 메뉴)");
                    System.out.println("(q 입력 시 메뉴로 돌아갑니다)");
                    System.out.println("-------------------------------");
                    String typeInput = scanner.nextLine().trim();

                    if (typeInput.equalsIgnoreCase("q")) {
                        System.out.println("메뉴로 돌아갑니다.");
                        return;
                    }

                    try {
                        accountType = Integer.parseInt(typeInput);

                        if (accountType == 3) {
                            System.out.println("모임통장은 아직 사용할 수 없는 메뉴입니다. 다른 옵션을 선택해주세요.");
                            continue;
                        } else if (accountType < 1 || accountType > 3) {
                            System.out.println("1, 2번 중 하나를 선택해주세요.");
                            continue;
                        }

                        // 적금 계좌 선택 시 151 계좌 여부 확인
                        if (accountType == 2) {
                            boolean hasAccount151 = user.hasAccount151();
                            if (!hasAccount151) {
                                System.out.println("적금 통장을 개설하려면 먼저 자유입출금 계좌(151)가 필요합니다.");
                                System.out.println("1번 메뉴를 선택하여 자유입출금 계좌를 먼저 생성해주세요.");
                                continue;
                            }
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("올바른 숫자를 입력해주세요.");
                    }
                }

                // 비밀번호 설정
                while (true) {
                    System.out.println();
                    System.out.println("계좌에 사용할 비밀번호 4자리를 입력해주세요.");
                    System.out.println("(q 입력 시 메뉴로 돌아갑니다.)");
                    password = scanner.nextLine().trim();

                    if (password.equals("q")) {
                        System.out.println("메뉴로 이동합니다.");
                        return;
                    }

                    // 비밀번호 길이 확인 및 숫자로만 구성되었는지 확인
                    if (password.length() != 4 || !password.matches("\\d+")) {
                        System.out.println("비밀번호는 4자리 숫자로만 구성되어야 합니다. 다시 입력해주세요.");
                        continue;
                    }

                    System.out.println("계좌에 사용할 비밀번호 4자리를 재입력해주세요.");
                    System.out.println("(q 입력 시 메뉴로 돌아갑니다.)");
                    passwordCheck = scanner.nextLine().trim();

                    if (passwordCheck.equals("q")) {
                        System.out.println("메뉴로 이동합니다.");
                        return;
                    }

                    // 비밀번호가 일치하는지 확인
                    if (!password.equals(passwordCheck)) {
                        System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
                        continue;
                    }
                    String accountTypeToString;
                    switch (accountType) {
                        case 1:
                            accountTypeToString = "151";
                            break;
                        case 2:
                            accountTypeToString = "152";
                            break;
                        default:
                            accountTypeToString = "153";
                            break;
                    }

                    // 계좌 생성
                    String accountNum = "";
                    boolean isValid = false;
                    while (!isValid) {
                        Random random = new Random();
                        StringBuilder num = new StringBuilder();
                        for (int i = 0; i < 8; i++) {
                            num.append(random.nextInt(10)); // 각 자리 0~9 랜덤 숫자 생성
                        }
                        accountNum = accountTypeToString + num.toString(); // 계좌 유형 + 8자리 랜덤 숫자
                        isValid = accountServiceRepository.checkNewAccount(accountNum);
                    }

                    Account account = null;
                    switch (accountType) {
                        case 1:
                            account = new Account151(user.getId(), user.getUsername(), accountNum, password, 0,todayDate.toString(),todayDate.toString(),"0");
                            account.setInterestRate();
                            break;
                        case 2:
                            String account152Type = selectInterestRate();
                            account = new Account152(user.getId(), user.getUsername(), accountNum, password, 0,todayDate.toString(),todayDate.toString(),account152Type);
                            account.setInterestRate();
                            break;
                    }

                    // 이자 날짜 설정
                    account.setLastInterestDate(todayDate.toString());

                    user.addAccount(account);
                    userServiceRepository.save(user);

                    userServiceRepository.updateUserFile("UserInfo.txt");
                    accountServiceRepository.addAccount(account);

                    System.out.println("계좌 개설이 완료되었습니다.");
                    System.out.println(user.getUsername() + "님의 계좌번호는 " + account.getAccountNum() + "입니다.");
                    System.out.println();
                    System.out.println("메인화면으로 돌아갑니다.");
                    break; // 모든 작업이 완료되면 반복 종료
                }
                break; // 계좌 생성 종료
            }
        } catch (Exception e) {
            System.out.println("잘못된 입력 형식입니다. 메뉴로 돌아갑니다.");
        }
    }

    public String selectInterestRate() {
        Scanner scanner = new Scanner(System.in);
        String accountType = "string";
        boolean validInput = false; // 입력 유효성을 확인하는 변수

        while (!validInput) { // 유효한 입력이 들어올 때까지 반복
            try {
                // 메뉴 출력
                System.out.println("1: 6개월 → 3%");
                System.out.println("2: 1년 → 4%");
                System.out.println("3: 2년 → 5%");
                System.out.print("원하는 옵션을 선택하세요 (1, 2, 3): ");
                int intrestRateChoice = scanner.nextInt();

                // 사용자의 선택에 따라 이자율 설정
                switch (intrestRateChoice) {
                    case 1:
                        accountType="1";
                        System.out.println("이자율이 3%로 설정되었습니다 (6개월)");
                        validInput = true; // 유효한 입력 처리
                        break;
                    case 2:
                        accountType="2";
                        System.out.println("이자율이 4%로 설정되었습니다 (1년)");
                        validInput = true; // 유효한 입력 처리
                        break;
                    case 3:
                        accountType="3";
                        System.out.println("이자율이 5%로 설정되었습니다 (2년)");
                        validInput = true; // 유효한 입력 처리
                        break;
                    default:
                        System.out.println("올바른 옵션을 선택하세요."); // 잘못된 입력 안내
                }
            } catch (InputMismatchException e) {
                System.out.println("숫자를 입력해야 합니다. 다시 시도하세요.");
                scanner.nextLine(); // 잘못된 입력 버퍼 비우기
            }
        }
        return accountType;
    }
}