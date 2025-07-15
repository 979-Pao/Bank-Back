package com.System.BankBack.controller;

import com.System.BankBack.dto.TransferDTO;
import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * End-points exclusivos para los Account Holders.
 * – GET    /holder/{id}/balance
 * – POST   /holder/transfer
 * – PATCH  /holder/{id}/status?status=ACTIVE|FROZEN
 * – DELETE /holder/{id}
 */
@RestController
@RequestMapping("/holder")
@RequiredArgsConstructor
public class AccountHolderController {

    private final AccountService accountService;

    /* ---------- GET: saldo de una cuenta ---------- */
    @GetMapping("/{id}/balance")
    public Money getBalance(@PathVariable Long id) {
        return accountService.getBalance(id);
    }

    /* ---------- POST: transferencia ---------- */
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transfer(@RequestBody TransferDTO dto) {
        accountService.transfer(dto);
    }

    /* ---------- PATCH: cambiar estado (ACTIVE / FROZEN) ---------- */
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeStatus(@PathVariable Long id,
                             @RequestParam String status) {
        accountService.changeStatus(id, status);
    }

    /* ---------- DELETE: cerrar cuenta ---------- */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeAccount(@PathVariable Long id) {
        accountService.closeAccount(id);
    }
}

