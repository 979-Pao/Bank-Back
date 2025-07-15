package com.System.BankBack.model.embedded;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@Data @NoArgsConstructor @AllArgsConstructor @Embeddable
public class Money {
    private BigDecimal amount; private Currency currency = Currency.getInstance("EUR");
    public Money(BigDecimal amount){this.amount=amount.setScale(2, RoundingMode.HALF_EVEN);} }

