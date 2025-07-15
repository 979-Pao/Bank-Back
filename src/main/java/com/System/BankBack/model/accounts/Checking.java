package com.System.BankBack.model.accounts;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity

public class Checking extends Account {
    private BigDecimal minimumBalance=BigDecimal.valueOf(250);
    private BigDecimal monthlyMaintenanceFee=BigDecimal.valueOf(12);

}

