package com.System.BankBack.dto;

import lombok.*;
import java.math.BigDecimal;

@Data public class ThirdPartyTransferDTO {
    private Long accountId;
    private String secretKey;
    private BigDecimal amount; }
