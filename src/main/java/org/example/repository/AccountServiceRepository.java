package org.example.repository;

import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.entity.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AccountServiceRepository {

    List<Account> accounts= new ArrayList<>();

    public AccountServiceRepository() { // 변경
        AccountFileReader("AccountInfo.txt");
    }

    // 계좌 저장 (새로 추가하거나 기존 계좌 업데이트)
    public void save(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountNum().equals(account.getAccountNum())) {
                accounts.set(i, account); // 이미 있으면 업데이트
                return;
            }
        }
        accounts.add(account); // 없으면 새로 추가
    }

    public void addAccount(Account account) {
        accounts.add(account);
        updateAccountFile("AccountInfo.txt");
    }

    public void removeAccount(String accountNum) {
        List<Account> accountList = new ArrayList<>(accounts); // 복사본 생성
        for (Account account : accountList) {
            if (account.getAccountNum().equals(accountNum)) {
                accounts.remove(account);
            }
        }
        updateAccountFile("AccountInfo.txt");
    }

    public Account getAccountByAccountNum(String accountNum) {
        for (Account account : accounts) {
            if (account.getAccountNum().equals(accountNum)) {
                return account;
            }
        }
        return null;
    }
    public List<Account> getAccountsByUserId(String userId) {
        List<Account> targetAccountList = new ArrayList<>();
        for (Account account : accounts) {
            if (account.getUserId().equals(userId)) {
                targetAccountList.add(account);
            }
        }
        return targetAccountList;
    }

    public List<Account> getAccountsByName(String userName) {
        List<Account> accountList = new ArrayList<>();
        for (Account account : accounts) {
            if (account.getName().equals(userName)) {
                accountList.add(account);
            }
        }
        return accountList;
    }

    public List<Account> getAccountsAll() {
        return accounts;
    }


    public void AccountFileReader(String filename) {
        Path path = Paths.get(filename);

        if (Files.notExists(path)) {
            System.out.println(filename + " 파일이 존재하지 않아 새로 생성합니다.");
            try {
                Files.createFile(path);
            } catch (IOException e) {
                System.err.println("파일 생성 중 오류 발생: " + e.getMessage());
                return;
            }
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 8) { // 8개의 필드 확인
                    try {
                        String userId = parts[0];
                        String name = parts[1];
                        String accountNum = parts[2];
                        String accountPw = parts[3];
                        int balance = Integer.parseInt(parts[4]);
                        String lastInterestDate = parts[5];
                        String makeDate = parts[6];
                        String accountType = parts[7];

                        Account account = new Account(userId, name, accountNum, accountPw, balance, lastInterestDate, makeDate, accountType);
                        account.setLastInterestDate(lastInterestDate);
                        accounts.add(account);
                    } catch (NumberFormatException e) {
                        System.err.println("숫자 형식 오류: " + line);
                    }
                } else {
                    System.err.println("잘못된 데이터 형식: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("파일 읽기 중 오류 발생: " + e.getMessage());
        }
    }

    public void updateAccountFile(String filename) {
        Path path = Paths.get(filename);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Account account : accounts) {
                String accountData = String.join("\t",
                        account.getUserId(),
                        account.getName(),
                        account.getAccountNum(),
                        account.getAccountPw(),
                        String.valueOf(account.getBalance()),
                        account.getLastInterestDate(),
                        account.getMakeDate(),
                        account.getAccountType()
                );
                writer.write(accountData);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("파일 업데이트 중 오류 발생: " + e.getMessage());
        }
    }


    public boolean checkNewAccount(String newAccountNum) {
        for (Account account : accounts) {
            if (account.getAccountNum().equals(newAccountNum)) {
                return false; // 이미 존재하는 계좌 번호
            }
        }
        return true; // 생성 가능한 계좌 번호
    }
}