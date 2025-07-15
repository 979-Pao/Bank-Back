package com.System.BankBack.model.users;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.*;

@EqualsAndHashCode(callSuper = true) @Data @NoArgsConstructor @Entity
public class Admin extends User { @PrePersist
void init(){setRole(com.System.BankBack.model.enums.RoleType.ADMIN);} }
