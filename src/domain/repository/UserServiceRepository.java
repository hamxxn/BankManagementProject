package domain.repository;
import domain.entity.Account;
import domain.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UserServiceRepository {

    List<User> users = new ArrayList<>();

    public UserServiceRepository() {
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

    public void UserFileReader(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                //System.out.println("유저 파일 라인 출력 : "+ line);
                String[] parts = line.split("\t");
                if (parts.length >= 5) {
                    String id = parts[0];
                    String password = parts[1];
                    String name= parts[2];
                    String phoneNum = parts[3];
                    String birth= parts[4];


                    ArrayList<Account> accounts = new ArrayList<>();
                    if (parts.length>=6) {
                        String accountData = parts[5];
                        //System.out.println("accountData: "+ accountData);
                        String[] accountentry = accountData.split(",");
                        for (String accentry : accountentry) {
                            //System.out.println("accentry: "+ accentry);
                            String[] accparts = accentry.split(" ");
                            if (accparts.length == 3) {
                                //System.out.println("accparts- 0 : "+ accparts[0]);
                                //System.out.println("accparts- 1 : "+ accparts[1]);
                                //System.out.println("accparts- 2 : "+ accparts[2]);

                                String accnum = accparts[0];
                                String accpw = accparts[1];
                                int accbalnce = Integer.parseInt(accparts[2]);
                                accounts.add(new Account(name, accnum, accpw, accbalnce));
                            }
                        }
                    }
                    else {
                        //System.out.println("계좌정보없음");
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


    public void updateUserFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                StringBuilder accountData= new StringBuilder();
                for (Account account : user.getAccounts()) {
                    System.out.println("계좌 번호: "+account.getAccountNum());
                    System.out.println("계좌 비번: "+account.getAccountPw());
                    System.out.println("계좌 잔액: "+account.getBalance());
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