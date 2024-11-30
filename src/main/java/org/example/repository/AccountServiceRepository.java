package org.example.repository;

import org.example.entity.Account;
import org.example.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 6) { // ID 포함 5개 필드 체크
                    String userId = parts[0];
                    String name = parts[1];
                    String accountNum = parts[2];
                    String accountPw = parts[3];
                    int balance = Integer.parseInt(parts[4]);
                    String lastinterestDate = parts[5];

                    Account account = new Account(userId, name, accountNum, accountPw, balance);
                    account.setLastInterestDate(lastinterestDate);
                    assert accounts != null;
                    accounts.add(account);
                }
            }
        } catch (IOException e) {
            System.err.println("파일을 찾을 수 없습니다.");
        } catch (NumberFormatException e) {
            System.err.println("잔액을 정수로 변환하는 중 오류 발생.");
        }
    }


    public void updateAccountFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Account account : accounts) {
                StringBuilder accountData = new StringBuilder();
                accountData.append(account.getUserId()).append("\t") // UserId 추가
                        .append(account.getName()).append("\t")
                        .append(account.getAccountNum()).append("\t")
                        .append(account.getAccountPw()).append("\t")
                        .append(account.getBalance()).append("\t")
                        .append(account.getLastInterestDate());;

                writer.write(accountData.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("파일 업데이트에 실패했습니다.");
        }
    }

}