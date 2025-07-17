package com.System.BankBack.repository;

import com.System.BankBack.model.users.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {
    ThirdParty findByHashKey(String hashKey);
}

