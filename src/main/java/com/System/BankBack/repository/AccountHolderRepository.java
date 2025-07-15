package com.System.BankBack.repository;

import com.System.BankBack.model.users.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> { AccountHolder findByUsername(String username);}
