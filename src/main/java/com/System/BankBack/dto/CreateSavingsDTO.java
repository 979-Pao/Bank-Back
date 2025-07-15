package com.System.BankBack.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateSavingsDTO {
    private BigDecimal initialBalance;
    private Long primaryOwnerId;
    private Long secondaryOwnerId;
    private String secretKey;
    private BigDecimal interestRate;
    private BigDecimal minimumBalance;
}
