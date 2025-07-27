package com.System.BankBack.model.users;

import com.System.BankBack.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "`user`")                 // «user» puede ser palabra reservada
public abstract class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    /** Se persiste como texto: ADMIN | ACCOUNTHOLDER | THIRD_PARTY */
    @Enumerated(EnumType.STRING)
    private RoleType role;

    public void setHashedKey(String s) {
    }
}

