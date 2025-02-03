package com.example.homework;

import java.math.BigDecimal;
import java.util.List;

public class TransactionProcessor {

    public void processTransaction(List<BankAccount> accounts, BigDecimal amount) {
        for (BankAccount a : accounts) {
            a.withdraw(amount);
        }
    }
}
