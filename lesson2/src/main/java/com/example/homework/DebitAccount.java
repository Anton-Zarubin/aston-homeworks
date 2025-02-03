package com.example.homework;

import java.math.BigDecimal;

public class DebitAccount extends BankAccount implements TransactionValidator {

    private static final BigDecimal TRANSACTION_LIMIT = new BigDecimal("10000.00");

    public DebitAccount(String accountNumber, BigDecimal balance, String accountHolder) {
        super(accountNumber, balance, accountHolder);
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (!validate(amount)) {
            System.out.println("Некорректное значение. Запрашиваемая сумма должна быть положительной и не превышать 10000.");
            return;
        }

        if (getBalance().compareTo(amount) >= 0) {
            setBalance(getBalance().subtract(amount));
            System.out.println("Операция по снятию средств с дебетового счёта выполнена. Остаток на счёте: " + getBalance());
        } else System.out.println("Недостаточно средств");
    }

    @Override
    public boolean validate(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0. && amount.compareTo(TRANSACTION_LIMIT) <= 0;
    }
}
