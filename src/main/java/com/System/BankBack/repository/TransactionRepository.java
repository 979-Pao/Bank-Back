package com.System.BankBack.repository;

import com.System.BankBack.model.transactions.Transaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByThirdPartyId(Long thirdPartyId);

    List<Transaction> findAllByThirdPartyHashKey(String hashKey);

    /* suma del día solicitado */
    @Query("""
           SELECT COALESCE(SUM(ABS(t.amount.amount)),0)
           FROM Transaction t
           WHERE t.account.id = :accId
             AND DATE(t.dateTime) = :day
           """)
    BigDecimal sumAmountByAccountAndDate(Long accId, LocalDate day);

    /* récord histórico (sin contar hoy) */
    @Query("""
           SELECT MAX(tot) FROM (
               SELECT SUM(ABS(t2.amount.amount)) AS tot
               FROM Transaction t2
               WHERE t2.account.id = :accId
                 AND DATE(t2.dateTime) < :day
               GROUP BY DATE(t2.dateTime)
           )""")
    BigDecimal maxDailyTotalBeforeDate(Long accId, LocalDate day);

    /* nº de movimientos en la ventana de 1 s */
    @Query("""
           SELECT COUNT(t)
           FROM Transaction t
           WHERE t.account.id = :accId
             AND t.dateTime BETWEEN :start AND :end
           """)
    long countByAccountAndTimeWindow(Long accId,
                                     LocalDateTime start,
                                     LocalDateTime end);

}
