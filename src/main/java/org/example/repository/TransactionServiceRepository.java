package org.example.repository;

import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.entity.User;

import java.io.*;
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

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void add(Transaction transaction) {
        transactionList.add(transaction);
        updateTransactionFile("TransactionLog.txt");
    }

    public void updateTransactionFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Transaction transaction : transactionList) {
                StringBuilder transactionData = new StringBuilder();
                transactionData.append(transaction.getDate()).append("\t") // 날짜
                        .append(transaction.getAccountNum()).append("\t")//계좌번호
                        .append(transaction.getTransactionType()).append("\t")         // 거래 유형
                        .append(transaction.getMemo()).append("\t")         // 메모
                        .append(transaction.getTransactionAmount()).append("\t") // 입출금 내역
                        .append(transaction.getBalance());                  // 잔액

                writer.write(transactionData.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("파일 업데이트에 실패했습니다.");
        }
    }

    public void TransactionFileReader(String filename) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 형식 지정

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 6) { // 필드 개수 확인
                    try {
                        // 데이터 필드 유효성 확인
                        if (parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank() ||
                                parts[3].isBlank() || parts[4].isBlank() || parts[5].isBlank()) {
                            System.err.println("필드가 비어 있습니다: " + line);
                            continue; // 유효하지 않은 데이터는 스킵
                        }

                        LocalDate date = LocalDate.parse(parts[0], dateFormatter); // 문자열을 LocalDate로 변환
                        String accountNum = parts[1];
                        String tranactionType = parts[2];
                        String memo = parts[3];
                        int transactionAmount = Integer.parseInt(parts[4]);
                        int balance = Integer.parseInt(parts[5]);

                        // Transaction 객체 생성
                        Transaction transaction = new Transaction(date,accountNum, tranactionType, memo, transactionAmount, balance);

                        // 리스트에 추가
                        transactionList.add(transaction);

                    } catch (DateTimeParseException e) {
                        System.err.println("잘못된 날짜 형식: " + parts[0]);
                    } catch (NumberFormatException e) {
                        System.err.println("잔액 또는 금액 변환 중 오류 발생: " + parts[4]);
                    }
                } else {
                    System.err.println("필드 수가 부족합니다: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("파일을 찾을 수 없습니다.");
        }
    }
}
