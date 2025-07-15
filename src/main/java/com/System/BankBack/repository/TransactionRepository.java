package com.System.BankBack.repository;

import com.System.BankBack.model.transactions.Transaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByThirdPartyId(Long thirdPartyId);

    List<Transaction> findAllByThirdPartyHashedKey(String hashedKey);
}
