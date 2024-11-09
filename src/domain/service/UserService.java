package domain.service;

import java.time.LocalDate;

public interface UserService {

    void deposit(LocalDate todayDate);

    void withdraw(LocalDate todayDate);

    void transfer(LocalDate todayDate);

    void createAccount();
}