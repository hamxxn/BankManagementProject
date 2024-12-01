package org.example.service;

import java.time.LocalDate;

public interface UserService {

    void myPage(LocalDate todayDate);

    void printMyPageMenu();

    void deposit(LocalDate todayDate);

    void withdraw(LocalDate todayDate);

    void transfer(LocalDate todayDate);

    void createAccount(LocalDate todayDate);
}
