package com.example.homework;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public abstract class BankAccount {

    protected String accountNumber;

    @Setter
    protected BigDecimal balance;

    protected String accountHolder;

    public abstract void withdraw(BigDecimal amount);

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Некорректное значение");
        } else {
            balance = balance.add(amount);
            System.out.println("Счёт пополнен. Доступно: " + getBalance());
        }
    };
}
