package com.System.BankBack.model.users;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true) @Data @NoArgsConstructor @Entity
public class ThirdParty extends User {
    @Column(nullable=false, unique=true) private String hashedKey;
    @PrePersist void init(){setRole(com.System.BankBack.model.enums.RoleType.THIRD_PARTY);} }