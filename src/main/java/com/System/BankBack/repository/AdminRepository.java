package com.System.BankBack.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<com.System.BankBack.model.users.Admin, Long> {
    com.System.BankBack.model.users.Admin findByUsername(String username);
}