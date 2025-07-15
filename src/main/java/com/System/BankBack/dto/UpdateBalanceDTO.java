package com.System.BankBack.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateBalanceDTO {
    private BigDecimal newBalance;
}
