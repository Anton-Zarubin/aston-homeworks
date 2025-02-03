package com.example.homework;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
@Getter
public class SavingsAccount extends BankAccount implements InterestBearing {

    private static final BigDecimal MONTHS_IN_YEAR = new BigDecimal("12");

    private BigDecimal interestRate;

    public SavingsAccount(String accountNumber, BigDecimal balance, String accountHolder, BigDecimal interestRate) {
        super(accountNumber, balance, accountHolder);
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Некорректное значение");
        } else if (getBalance().compareTo(amount) >= 0) {
            setBalance(getBalance().subtract(amount));
            System.out.println("Операция по снятию средств с накопительного счёта выполнена. Остаток на счёте: " + getBalance());
        } else System.out.println("Недостаточно средств");
    }

    @Override
    public void applyInterest() {
        BigDecimal interest = getBalance().multiply(interestRate).divide(MONTHS_IN_YEAR, 2, RoundingMode.HALF_EVEN);
        deposit(interest);
    }
}
