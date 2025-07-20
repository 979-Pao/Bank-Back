package com.System.BankBack.repository;

import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Buscar cuentas por titular principal
    List<Account> findByPrimaryOwner(AccountHolder holder);

    // Buscar cuentas donde el titular secundario sea X (opcional, por si lo necesitás)
    List<Account> findBySecondaryOwner(AccountHolder holder);

    /** Devuelve todas las cuentas donde el titular dado sea
     *  owner principal **o** secundario                                           */
    @Query("""
           SELECT a
           FROM   Account a
           WHERE  a.primaryOwner = :holder
              OR  a.secondaryOwner = :holder
           """)
    List<Account> findAllByHolder(@Param("holder") AccountHolder holder);

}
