package domain.repository;

import domain.entity.Account;
import domain.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UserServiceRepository {

    private final List<User> users;

    public UserServiceRepository() {
        this.users = new ArrayList<User>();
        UserFileReader("UserInfo.txt");
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

    private void UserFileReader(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 6) {
                    String id = parts[0];
                    String password = parts[1];
                    String name= parts[2];
                    String phoneNum = parts[3];
                    String birth= parts[4];

                    String accountData = parts[5];
                    ArrayList<Account> accounts = new ArrayList<>();
                    String[] accountentry = accountData.split(",");
                    for (String accentry : accountentry) {
                        String[] accparts = accentry.split(" ");
                        if (accparts.length == 3) {
                            String accnum = accparts[0];
                            String accpw = accparts[1];
                            int accbalnce = Integer.parseInt(accparts[2]);
                            accounts.add(new Account(name, accnum, accpw, accbalnce));
                        }
                    }

                    User user = new User(id, password, name, phoneNum, birth, accounts);
                    assert users != null;
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("파일을 찾을 수 없습니다.");
        }
    }


    private void updateUserFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                StringBuilder accountData= new StringBuilder();
                for (Account account : user.getAccounts()) {
                    accountData.append(account.getAccountNum())
                            .append(" ")
                            .append(account.getAccountPw())
                            .append(" ")
                            .append(account.getBalance())
                            .append(",");
                }
                if (accountData.length() > 0) {
                    accountData.setLength(accountData.length() - 1);
                }

                writer.write(user.getId() + "\t" + user.getPassword()+ "\t" +
                        user.getUsername()  + "\t" + user.getPhoneNum() + "\t" +
                        user.getBirth() + "\t" + accountData);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("파일 업데이트에 실패했습니다.");
        }
    }


}