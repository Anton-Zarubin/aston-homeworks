package com.example.homework;

import java.math.BigDecimal;

public interface TransactionValidator {

    boolean validate(BigDecimal amount);
}
