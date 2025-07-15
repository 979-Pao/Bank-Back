package com.System.BankBack.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferDTO {
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
}