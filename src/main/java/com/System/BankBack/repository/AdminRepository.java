package com.System.BankBack.repository;

import com.System.BankBack.model.users.Admin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<com.System.BankBack.model.users.Admin, Long> {
    com.System.BankBack.model.users.Admin findByUsername(String username);
}