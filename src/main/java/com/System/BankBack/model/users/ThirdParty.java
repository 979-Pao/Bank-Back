package com.System.BankBack.model.users;

import com.System.BankBack.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class ThirdParty extends User {

    @Column(nullable = false, unique = true)
    private String hashKey;

    @PrePersist
    void init() {
        setRole(RoleType.THIRD_PARTY);
    }
}
