package com.System.BankBack.controller;

import com.System.BankBack.dto.TransferDTO;
import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.users.AccountHolder;
import com.System.BankBack.repository.AccountHolderRepository;
import com.System.BankBack.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/holder")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ACCOUNTHOLDER')")             // se aplica a todos los métodos
public class AccountHolderController {

    private final AccountService          accountService;
    private final AccountHolderRepository holderRepo;

    /* ─────────────────────────── GET ─────────────────────────── */

    /** ①  Lista TODAS las cuentas del titular autenticado */
    @GetMapping("/accounts")
    public List<Account> myAccounts() {
        return accountService.listAccountsForHolder(currentHolder().getUsername());
    }

    /** ②  Saldo de UNA cuenta concreta (solo si es suya) */
    @GetMapping("/account/{accountId}")
    public Account getBalance(@PathVariable Long accountId) {
        AccountHolder holder = currentHolder();
        return accountService.getAccountIfOwnedByHolder(accountId, holder.getId());
    }

    /* ────────────────────────── POST ─────────────────────────── */

    /** Transferencia entre cuentas */
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transfer(@RequestBody TransferDTO dto) {
        accountService.transfer(dto);
    }

    /* ───────────────────────── PATCH ─────────────────────────── */

    /** Cambiar estado de una cuenta (ACTIVE / FROZEN) */
    @PatchMapping("/{accountId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeStatus(@PathVariable Long accountId,
                             @RequestParam String status) {
        AccountHolder holder = currentHolder();
        accountService.changeStatusIfOwnedByHolder(accountId, holder.getId(), status);
    }

    /* ───────────────────────── DELETE ────────────────────────── */

    /** Cerrar (eliminar) una cuenta */
    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeAccount(@PathVariable Long accountId) {
        AccountHolder holder = currentHolder();
        accountService.closeAccountIfOwnedByHolder(accountId, holder.getId());
    }

    /* ──────────────────────── Helpers ───────────────────────── */

    /** Devuelve el AccountHolder que hay dentro del token JWT */
    private AccountHolder currentHolder() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return holderRepo.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found"));
    }
}
