package org.example.repository;

import org.example.entity.Account;
import org.example.entity.User;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserServiceRepository {

    List<User> users = new ArrayList<>();
    List<String> loginHistory = new ArrayList<>();
    AccountServiceRepository accountServiceRepository;
    public UserServiceRepository(AccountServiceRepository accountServiceRepository) {
        this.accountServiceRepository = accountServiceRepository;
        UserFileReader("UserInfo.txt");
    }

    // 사용자를 저장하는 메서드
    public void save(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user); // 이미 있으면 업데이트
                return;
            }
        }
        users.add(user); // 없으면 새로 추가
    }


    public void updateUserBalance(Account updatedAccount) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountNum().equals(updatedAccount.getAccountNum())) {
                    account.setBalance(updatedAccount.getBalance());
                    account.setLastInterestDate(updatedAccount.getLastInterestDate());
                    updateUserFile("UserInfo.txt"); // 파일 동기화
                    return;
                }
            }
        }
    }


    public User getUserByAccount(String accountnum) {
        for (User user : users) {
            for (Account userAccount : user.getAccounts()) {
                if (userAccount.getAccountNum().equals(accountnum)) {
                    return user; // 계좌를 가진 사용자 반환
                }
            }
        }
        return null; // 계좌가 없으면 null 반환
    }

    public void add(User user) {
        users.add(user);
        updateUserFile("UserInfo.txt");
    }

    public User getUserById(String id) {
        for(User user : users){
            if(Objects.equals(user.getId(), id)){
                return user;
            }
        }
        return null;
    }

    public User getUserByPhoneNumber(String phoneNum) {
        for(User user : users){
            if(Objects.equals(user.getPhoneNum(), phoneNum)){
                return user;
            }
        }
        return null;
    }

    public List<User> getUserByName(String name) {
        List<User> getUser = new ArrayList<>();
        for(User user : users) {
            if(Objects.equals(user.getUsername(), name)) {
                getUser.add(user);
            }
        }
        return getUser;
    }

    public void LoginFileReader(String filename) {
        loginHistory.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                loginHistory.add(line);
            }
        } catch (IOException e) {
            System.err.println("로그인 기록 파일을 읽을 수 없습니다: " + filename);
        }
    }

    public void addLoginRecord(String filename, String userid, LocalDate logindate) {
        String record = userid + "\t" + logindate.toString();
        loginHistory.add(record);
        updateLoginFile(filename);
    }

    public void updateLoginFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String record : loginHistory) {
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("로그인 기록 파일을 업데이트할 수 없습니다: " + filename);
        }
    }

    public LocalDate getLastLogin(String filename) {
        if (loginHistory.isEmpty()) {
            return null;
        }
        String last = loginHistory.get(loginHistory.size()-1);
        String[] parts = last.split("\t");
        if (parts.length>=2) {
            return LocalDate.parse(parts[1]);
        } else {
            return null;
        }

    }

    public void UserFileReader(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 5) {
                    String id = parts[0];
                    String password = parts[1];
                    String name = parts[2];
                    String phoneNum = parts[3];
                    String birth = parts[4];

                    List<Account> accounts = new ArrayList<>();
                    if (parts.length >= 6) { // 계좌 정보가 있을때
                        //String accountData = parts[5];
                        accounts = accountServiceRepository.getAccountsByUserId(id);
                    }

                    User user = new User(id, password, name, phoneNum, birth, (ArrayList<Account>) accounts);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("파일을 찾을 수 없습니다: " + e.getMessage());
        }
    }



    public void updateUserFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                StringBuilder accountData= new StringBuilder();
                for (Account account : user.getAccounts()) {
                    accountData.append(account.getAccountNum())
                            .append(" ").append(account.getAccountPw())
                            .append(" ").append(account.getBalance())
                            .append(",");
                }

                writer.write(user.getId() + "\t" + user.getPassword()+ "\t" +
                        user.getUsername()  + "\t" + user.getPhoneNum() + "\t" +
                        user.getBirth());
                if (!accountData.isEmpty()) {
                    accountData.deleteCharAt(accountData.length()-1);
                    writer.write("\t"+accountData);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("파일 업데이트에 실패했습니다.");
        }
    }


}