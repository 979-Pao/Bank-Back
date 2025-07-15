package com.System.BankBack.service;

import com.System.BankBack.dto.*;
import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.embedded.Money;
import java.util.List;

public interface AccountService {
    Money getBalance(Long accountId);
    void transfer(TransferDTO dto);
    /** Cambiar el estado de la cuenta (ACTIVE / FROZEN). */
    void changeStatus(Long accountId, String status);
    /** Cerrar (eliminar) la cuenta del titular. */
    void closeAccount(Long accountId);
}