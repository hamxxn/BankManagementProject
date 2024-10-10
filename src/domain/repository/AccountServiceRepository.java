
package domain.repository;

import domain.entity.Account;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AccountServiceRepository {

    private final List<Account> accounts;

    public AccountServiceRepository() { // 변경
        AccountFileReader("../AccountInfo.txt");
        this.accounts = new ArrayList<Account>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
        updateAccountFile("../AccountInfo.txt");
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

    private void AccountFileReader(String filename) {
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


    private void updateAccountFile(String filename) {
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
