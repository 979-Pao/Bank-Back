package com.System.BankBack.controller;

import com.System.BankBack.dto.*;
import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.users.AccountHolder;
import com.System.BankBack.model.users.ThirdParty;
import com.System.BankBack.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * End-points exclusivos para los administradores (ADMIN).
 * – POST   /admin/checking        → Crear cuenta Checking o StudentChecking (según edad)
 * – POST   /admin/savings         → Crear cuenta Savings
 * – POST   /admin/credit-card     → Crear tarjeta de crédito
 * – POST   /admin/holders         → Crear un titular de cuenta (AccountHolder)
 * – POST   /admin/third-party     → Crear un usuario de terceros (ThirdParty)
 * – GET    /admin/accounts        → Listar todas las cuentas del sistema
 * – GET    /admin/holders         → Listar todos los titulares (AccountHolder)
 * – GET    /admin/third-parties   → Listar todos los usuarios ThirdParty
 * – PUT    /admin/accounts/{id}/balance  → Actualizar el saldo de una cuenta específica
 * – DELETE /admin/accounts/{id}  → Eliminar una cuenta por su ID
 */


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    /* ---------- POST ---------- */
    /** Crear Checking */
    @PostMapping("/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createChecking(@RequestBody CreateCheckingDTO dto){
        return adminService.createChecking(dto);
    }

    /** Crear Savings */
    @PostMapping("/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createSavings(@RequestBody CreateSavingsDTO dto){
        return adminService.createSavings(dto);
    }

    /** Crear Credit Card */
    @PostMapping("/credit-card")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createCC(@RequestBody CreateCreditCardDTO dto){
        return adminService.createCreditCard(dto);
    }

    /** Crear AccountHolder */
    @PostMapping("/holders")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder createHolder(@RequestBody CreateAccountHolderDTO dto) {
        return adminService.createAccountHolder(dto);
    }

    /** Crear ThirdParty */
    @PostMapping("/third-party")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty createThirdParty(@RequestBody CreateThirdPartyDTO dto) {
        return adminService.createThirdParty(dto);
    }

    /* ---------- GET ---------- */
    /** Listar todas las cuentas */
    @GetMapping("/accounts")
    public List<Account> all(){
        return adminService.findAllAccounts(); }

    /** Listar todos los titulares (AccountHolder) */
    @GetMapping("/holders")                       //
    public List<AccountHolder> allHolders() {
        return adminService.findAllHolders();
    }

    /** Listar todos los terceros (Thirdparty) */
    @GetMapping("/third-parties")
    public List<ThirdParty> allThirdParties() {
        return adminService.findAllThirdParties();
    }

    /* ---------- PUT / PATCH ---------- */
    /** Actualizar saldo de una cuenta */
    @PutMapping("/accounts/{id}/balance")
    public Account updateBalance(@PathVariable Long id,
                                 @RequestBody UpdateBalanceDTO dto){
        return adminService.updateBalance(id,dto);
    }

    /* ---------- DELETE ---------- */
    /** Borrar una cuenta */
    @DeleteMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){ adminService.deleteAccount(id);}
}

