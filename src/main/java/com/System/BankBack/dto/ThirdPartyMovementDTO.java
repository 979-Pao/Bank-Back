package com.System.BankBack.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ThirdPartyMovementDTO {
    private Long accountId;
    private String secretKey;
    private BigDecimal amount;
}
