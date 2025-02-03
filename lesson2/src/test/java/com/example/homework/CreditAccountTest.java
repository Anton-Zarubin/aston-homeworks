package com.example.homework;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditAccountTest {

    private static final BigDecimal INIT_BALANCE = new BigDecimal("40.00");

    CreditAccount creditAccount = new CreditAccount("2", INIT_BALANCE, "some name",
            new BigDecimal("-5000.00"), new BigDecimal("0.01"));

    @ParameterizedTest
    @ArgumentsSource(WithdrawalArgumentsProvider.class)
    public void testWithdraw(BigDecimal amount, BigDecimal balance) {
        creditAccount.withdraw(amount);
        assertEquals(balance, creditAccount.getBalance());
    }

    static class WithdrawalArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(new BigDecimal("5500.00"), INIT_BALANCE),
                    Arguments.of(new BigDecimal("5000.00"), INIT_BALANCE),
                    Arguments.of(new BigDecimal("3000.00"), new BigDecimal("-2990.00"))
            );
        }
    }
}
