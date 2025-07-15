package com.System.BankBack.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateCreditCardDTO {
    private BigDecimal initialBalance;
    private Long primaryOwnerId;
    private Long secondaryOwnerId;
    private BigDecimal creditLimit;
    private BigDecimal interestRate;
}
