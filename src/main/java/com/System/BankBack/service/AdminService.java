package com.System.BankBack.service;

import com.System.BankBack.dto.*;
import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.users.AccountHolder;   // ← FALTA ESTE IMPORT
import com.System.BankBack.model.users.ThirdParty;

import java.util.List;

/**
 * Operaciones exclusivas del rol ADMIN.
 */
public interface AdminService {

    /* ---------- POST ---------- */
    /** Crear un nuevo AccountHolder */
    AccountHolder createAccountHolder(CreateAccountHolderDTO dto);
    /** Crear un nuevo ThirdParty */
    ThirdParty createThirdParty(CreateThirdPartyDTO dto);
    /** Crear un Checking */
    Account createChecking(CreateCheckingDTO dto);
    /** Crear un Savings */
    Account createSavings(CreateSavingsDTO dto);
    /** Crear una Credit Card */
    Account createCreditCard(CreateCreditCardDTO dto);

    /* ---------- GET ---------- */
    /** Listar todas las cuentas del sistema */
    List<Account> findAllAccounts();
    List<AccountHolder> findAllHolders();

    /** Listar todas los terceros */
    List<ThirdParty> findAllThirdParties();

    /* ---------- PUT / PATCH ---------- */
    /** Actualizar el balance de una cuenta */
    Account updateBalance(Long id, UpdateBalanceDTO dto);
    /* ---------- DELETE ---------- */

    /** Eliminar una cuenta por ID */
    void deleteAccount(Long id);
}
