package domain.repository;

import domain.dto.UserInfo;
import domain.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class UserServiceRepository {

    private List<User> users;

    public UserServiceRepository() {
        UserfileReader("./UserInfo.txt");
        AccountfileReader("./AccountInfo.txt");
        this.users = new ArrayList<User>();
    }

    public void add(User user) {
        users.add(user);
        updateUserFile("./UserInfo.txt");
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


    private void UserfileReader(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 5) {
                    String id = parts[0];
                    String password = parts[1];
                    String name = parts[2];
                    String phoneNumber = parts[3];
                    String birth = parts[4];

                    User user = new User(id, password, name, phoneNumber, birth);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("파일을 찾을 수 없습니다.");
        }
    }


    private void AccountfileReader(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 4) {
                    String accountNum = parts[0];
                    String userId = parts[1];
                    int accountpw =parseInt(parts[2]);
                    int balance = parseInt(parts[3]);

                    User user = getUserById(userId);
//                    if (user != null) {
//                        Account account = new Account(accountNum, accountpw, balance);
//                        user.getAccounts().add(account);
//                    }
                }
            }
        } catch (IOException e) {
            System.err.println("파일을 찾을 수 없습니다.");
        }
    }

    private void updateUserFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                writer.write(user.getId() + "\t" +
                        user.getPassword() + "\t"
                        + user.getUsername() + "\t"  + user.getPhoneNum() + "\t" +
                        user.getBirth());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("파일 업데이트에 실패했습니다.");
        }
    }
}
