package com.example.homework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DebitAccountTest {

    private static final BigDecimal INIT_BALANCE = new BigDecimal("20000.00");

    DebitAccount debitAccount = new DebitAccount("1", INIT_BALANCE, "some name");

    @ParameterizedTest
    @ArgumentsSource(WithdrawalArgumentsProvider.class)
    public void testWithdraw(BigDecimal amount, BigDecimal balance) {
        debitAccount.withdraw(amount);
        assertEquals(balance, debitAccount.getBalance());
    }

    static class WithdrawalArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(new BigDecimal("11000.00"), INIT_BALANCE),
                    Arguments.of(new BigDecimal("21000.00"), INIT_BALANCE),
                    Arguments.of(new BigDecimal("7000.00"), new BigDecimal("13000.00"))
            );
        }
    }

    @Test
    public void testDeposit() {
        debitAccount.deposit(new BigDecimal("1000.00"));
        assertEquals(new BigDecimal("21000.00"), debitAccount.getBalance());
    }
}
