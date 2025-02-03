package com.example.homework;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        TransactionProcessor transactionProcessor = new TransactionProcessor();

        List<BankAccount> accounts = List.of(new DebitAccount("1", new BigDecimal("20000.00"), "some name"),
                new CreditAccount("2", new BigDecimal("2000.00"), "some name", new BigDecimal("-5000.00"), new BigDecimal("0.01")),
                new SavingsAccount("3", new BigDecimal("20000.00"), "some name", new BigDecimal("0.18")));

        transactionProcessor.processTransaction(accounts, new BigDecimal("-1000.00"));
        transactionProcessor.processTransaction(accounts, new BigDecimal("3000.00"));
        transactionProcessor.processTransaction(accounts, new BigDecimal("5000.00"));
        transactionProcessor.processTransaction(accounts, new BigDecimal("11000.00"));
    }
}
