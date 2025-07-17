package com.System.BankBack.model.accounts;

import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.model.enums.Status;
import com.System.BankBack.model.users.AccountHolder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Checking.class, name = "Checking"),
        @JsonSubTypes.Type(value = StudentChecking.class, name = "StudentChecking"),
        @JsonSubTypes.Type(value = Savings.class, name = "Savings"),
        @JsonSubTypes.Type(value = CreditCard.class, name = "CreditCard")
})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account {

    /* ───────────── Identificación y relaciones ───────────── */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Money balance;

    @ManyToOne(optional = false)
    private AccountHolder primaryOwner;

    @ManyToOne
    private AccountHolder secondaryOwner;

    /* ───────────── Seguridad y configuración ─────────────── */
    /** Clave secreta para operaciones de terceros (nunca nula) */
    @Column(name = "secret_key", nullable = false)
    private String secretKey;

    private BigDecimal penaltyFee = BigDecimal.valueOf(40);

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private LocalDate creationDate = LocalDate.now();

    /* ───────────── Hooks de ciclo de vida ───────────── */
    /** Asegura que siempre exista un secretKey antes de persistir */
    @PrePersist
    private void ensureSecretKey() {
        if (secretKey == null || secretKey.isBlank()) {
            this.secretKey = "ACC-" + UUID.randomUUID();
        }
    }
}
