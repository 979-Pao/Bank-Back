package com.System.BankBack.model.accounts;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity

public class CreditCard extends Account {
    private BigDecimal creditLimit=BigDecimal.valueOf(100);
    private BigDecimal interestRate=BigDecimal.valueOf(0.2);

}


