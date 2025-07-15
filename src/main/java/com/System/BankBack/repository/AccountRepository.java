package com.System.BankBack.repository;

import com.System.BankBack.model.accounts.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {}




