package org.example.repository;

import org.example.entity.Account;
import org.example.entity.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TransactionServiceRepository {
    List<Transaction> transactionList= new ArrayList<>();

    public TransactionServiceRepository() {
        TransactionFileReader("TransactionLog.txt");
    }

    // 새로운 입출금 내역 저장
    public void save(Transaction transaction) {
        transactionList.add(transaction);
    }

    public void TransactionFileReader(String filename) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 형식 지정

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 7) { // 필드 개수 확인
                    try {
                        LocalDate date = LocalDate.parse(parts[0], dateFormatter); // 문자열을 LocalDate로 변환
                        String userId = parts[1];
                        String name = parts[2];
                        String accountNum = parts[3];
                        String transactionDetail = parts[4];
                        String memo = parts[5];
                        int balance = Integer.parseInt(parts[6]);

                        // 입출금 내역 검증: "+금액" 또는 "-금액" 형식 확인
                        if (!transactionDetail.matches("[+-]\\d+")) {
                            System.err.println("잘못된 입출금 내역 형식: " + transactionDetail);
                            continue; // 유효하지 않은 데이터는 스킵
                        }

                        // Transaction 객체 생성 및 처리
                        Transaction transaction = new Transaction(date, userId, name, accountNum,
                                transactionDetail, memo, balance);
                        // 여기에 Transaction 리스트나 다른 처리를 추가
                    } catch (DateTimeParseException e) {
                        System.err.println("잘못된 날짜 형식: " + parts[0]);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("파일을 찾을 수 없습니다.");
        } catch (NumberFormatException e) {
            System.err.println("잔액 또는 금액 변환 중 오류 발생.");
        }
    }
}
