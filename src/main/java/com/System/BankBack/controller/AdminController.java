package com.System.BankBack.controller;

import com.System.BankBack.dto.*;
import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.users.AccountHolder;
import com.System.BankBack.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

