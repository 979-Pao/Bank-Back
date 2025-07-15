package com.System.BankBack.model.users;

import com.System.BankBack.model.embedded.Address;
import com.System.BankBack.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * AccountHolder: hereda de User usando estrategia JOINED.
 * Se mapea con @PrimaryKeyJoinColumn para que comparta la PK "id" con la tabla USER.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class AccountHolder extends User {

    private LocalDate dateOfBirth;

    /* Dirección principal */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street",      column = @Column(name = "primary_street")),
            @AttributeOverride(name = "city",        column = @Column(name = "primary_city")),
            @AttributeOverride(name = "postalCode",  column = @Column(name = "primary_postal_code")),
            @AttributeOverride(name = "country",     column = @Column(name = "primary_country"))
    })
    private Address primaryAddress;

    /* Dirección de correo (opcional) */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street",      column = @Column(name = "mailing_street")),
            @AttributeOverride(name = "city",        column = @Column(name = "mailing_city")),
            @AttributeOverride(name = "postalCode",  column = @Column(name = "mailing_postal_code")),
            @AttributeOverride(name = "country",     column = @Column(name = "mailing_country"))
    })
    private Address mailingAddress;

    @PrePersist
    void init() {
        setRole(RoleType.ACCOUNTHOLDER);
    }
}