package com.System.BankBack.repository;

import com.System.BankBack.model.users.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {
    ThirdParty findByHashedKey(String hashedKey);
}
