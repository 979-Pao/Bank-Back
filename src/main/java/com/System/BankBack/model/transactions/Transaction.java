package com.System.BankBack.model.transactions;

import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.model.users.ThirdParty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    /* ---------- columnas básicas ---------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cuenta sobre la que se aplica el movimiento (+ / −) */
    @ManyToOne(optional = false)
    private Account account;

    /** Importe con signo (positivo o negativo) */
    @Embedded
    private Money amount;

    /** Fecha/hora del asiento */
    private LocalDateTime dateTime = LocalDateTime.now();

    /* ---------- relación opcional ---------- */
    /** Tercera parte que originó la operación (nullable) */
    @ManyToOne
    private ThirdParty thirdParty;

    /* ---------- constructores de conveniencia ---------- */

    /** Para holders o admin (sin third-party, sin id explícito) */
    public Transaction(Account account, Money amount, LocalDateTime dateTime) {
        this.account  = account;
        this.amount   = amount;
        if (dateTime != null) this.dateTime = dateTime;
    }

    /** Para holders o admin (con id forzado—raro, solo si lo necesitas) */
    public Transaction(Long id, Account account, Money amount, LocalDateTime dateTime) {
        this(account, amount, dateTime);
        this.id = id;
    }

    /** Específico para terceros */
    public Transaction(ThirdParty thirdParty,
                       Account account,
                       Money amount,
                       LocalDateTime dateTime) {
        this(account, amount, dateTime);
        this.thirdParty = thirdParty;
    }
}