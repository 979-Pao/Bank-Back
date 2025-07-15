package com.System.BankBack.model.accounts;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Savings extends Account {
    private BigDecimal minimumBalance=BigDecimal.valueOf(1000);
    private BigDecimal interestRate=BigDecimal.valueOf(0.0025);

}

