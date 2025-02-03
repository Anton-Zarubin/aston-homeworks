package com.example.homework;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
@Getter
public class CreditAccount extends BankAccount implements TransactionFee, TransactionValidator {

    private static final BigDecimal TRANSACTION_LIMIT = new BigDecimal("5000.00");

    private BigDecimal creditLimit;

    private BigDecimal feeRate;

    public CreditAccount(String accountNumber, BigDecimal balance, String accountHolder, BigDecimal creditLimit, BigDecimal feeRate) {
        super(accountNumber, balance, accountHolder);
        this.creditLimit = creditLimit;
        this.feeRate = feeRate;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (!validate(amount)) {
            System.out.println("Некорректное значение. Запрашиваемая сумма должна быть положительной и не превышать 5000.");
            return;
        }

        BigDecimal totalAmount = applyFee(amount);
        if ((getBalance().subtract(totalAmount)).compareTo(creditLimit) >= 0) {
            setBalance(getBalance().subtract(totalAmount));
            System.out.println("Операция по снятию средств с кредитного счёта выполнена. Остаток на счёте: " + getBalance());
        } else System.out.println("Недостаточно средств");
    }

    @Override
    public BigDecimal applyFee(BigDecimal amount) {
        return amount.multiply(BigDecimal.ONE.add(feeRate)).setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public boolean validate(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0. && amount.compareTo(TRANSACTION_LIMIT) <= 0;
    }
}
