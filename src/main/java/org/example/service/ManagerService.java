package org.example.service;

import java.time.LocalDate;

public interface ManagerService {
    void accountSearch();
    void showAccountList();
    void showAccountHistory(LocalDate todayDate);
}