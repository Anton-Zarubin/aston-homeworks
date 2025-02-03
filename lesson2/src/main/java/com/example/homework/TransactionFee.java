package com.example.homework;

import java.math.BigDecimal;

public interface TransactionFee {

    BigDecimal applyFee(BigDecimal amount);
}
