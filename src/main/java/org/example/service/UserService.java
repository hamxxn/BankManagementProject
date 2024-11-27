package org.example.service;

import java.time.LocalDate;

public interface UserService {

    void myPage();

    void printMyPageMenu();

    void deposit();

    void withdraw();

    void transfer(LocalDate todayDate);

    void createAccount();
}
