package com.System.BankBack.service;

import com.System.BankBack.dto.*;
import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.embedded.Money;

import java.util.List;

public interface AccountService {
    Money getBalance(Long accountId);
    /** Todas las cuentas (primary+secondary) de un titular dado su username */
    List<Account> listAccountsForHolder(String username);
    /** (Legacy) Todas las cuentas de un titular por su ID */
    List<Account> findByHolderId(Long holderId);
    /** Devuelve la cuenta solo si pertenece al titular indicado */
    Account getAccountIfOwnedByHolder(Long accountId, Long holderId);

    void transfer(TransferDTO dto);
    /** Cambiar el estado de la cuenta (ACTIVE / FROZEN). */
    void changeStatus(Long accountId, String status);
    /** Cerrar (eliminar) la cuenta del titular. */
    void closeAccount(Long accountId);

    void changeStatusIfOwnedByHolder(Long accountId, Long holderId, String status);
    void closeAccountIfOwnedByHolder(Long accountId, Long holderId);

}