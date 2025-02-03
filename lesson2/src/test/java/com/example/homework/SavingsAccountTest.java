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

public class SavingsAccountTest {

    private static final BigDecimal INIT_BALANCE = new BigDecimal("20000.00");

    SavingsAccount savingsAccount = new SavingsAccount("3", INIT_BALANCE, "some name", new BigDecimal("0.18"));

    @ParameterizedTest
    @ArgumentsSource(WithdrawalArgumentsProvider.class)
    public void testWithdraw(BigDecimal amount, BigDecimal balance) {
        savingsAccount.withdraw(amount);
        assertEquals(balance, savingsAccount.getBalance());
    }

    static class WithdrawalArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(new BigDecimal("-1000.00"), INIT_BALANCE),
                    Arguments.of(new BigDecimal("21000.00"), INIT_BALANCE),
                    Arguments.of(new BigDecimal("5000.00"), new BigDecimal("15000.00"))
            );
        }
    }

    @Test
    public void testApplyInterest(){
        savingsAccount.applyInterest();
        assertEquals(new BigDecimal("20300.00"), savingsAccount.getBalance());
    }
}
