package domain.repository;

import domain.entity.Account;
import domain.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
                if (parts.length >= 4) {
                    String name= parts[0];
                    String accountNum = parts[1];
                    String accountPw= parts[2];
                    int balance = Integer.parseInt(parts[3]);

                    Account account = new Account(name,accountNum, accountPw, balance);
                    assert accounts != null;
                    accounts.add(account);
                }

            }
        } catch (IOException e) {
            System.err.println("파일을 찾을 수 없습니다.");
        }
    }


    public void updateAccountFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Account account : accounts) {
                writer.write(account.getName() + "\t"+ account.getAccountNum() + "\t" + account.getAccountPw() + "\t" + account.getBalance());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("파일 업데이트에 실패했습니다.");
        }
    }
}