package com.System.BankBack.service.impl;

import com.System.BankBack.dto.TransferDTO;
import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.model.enums.Status;
import com.System.BankBack.model.transactions.Transaction;
import com.System.BankBack.model.users.AccountHolder;
import com.System.BankBack.repository.*;
import com.System.BankBack.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository       accountRepo;
    private final TransactionRepository   txRepo;
    private final AccountHolderRepository holderRepo;

    /* ═════ HELPERS ═════ */

    private AccountHolder getHolderById(Long id) {
        return holderRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Holder not found"));
    }

    private AccountHolder getHolderByUsername(String username) {
        return holderRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Holder not found"));
    }

    private void assertOwnership(Account acc, Long holderId) {
        boolean owns = acc.getPrimaryOwner().getId().equals(holderId) ||
                (acc.getSecondaryOwner() != null &&
                        acc.getSecondaryOwner().getId().equals(holderId));
        if (!owns) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You do not own this account");
        }
    }

    /* ═════ CONSULTAS ═════ */

    @Override
    public Money getBalance(Long accountId) {
        return accountRepo.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account not found"))
                .getBalance();
    }

    /** Devuelve TODAS las cuentas (primary + secondary) de un titular */
    @Override
    public List<Account> listAccountsForHolder(String username) {
        AccountHolder holder = getHolderByUsername(username);
        return accountRepo.findAllByHolder(holder);
    }

    /** Versión antigua (usada por algún código legacy)               */
    @Override
    public List<Account> findByHolderId(Long holderId) {
        AccountHolder holder = getHolderById(holderId);
        return accountRepo.findAllByHolder(holder);
    }

    @Override
    public Account getAccountIfOwnedByHolder(Long accountId, Long holderId) {
        Account acc = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account not found"));
        assertOwnership(acc, holderId);
        return acc;
    }

    /* ═════ TRANSFERENCIAS ═════ */

    @Override
    @Transactional
    public void transfer(TransferDTO dto) {

        Account from = accountRepo.findById(dto.getFromAccountId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "From-account not found"));

        Account to = accountRepo.findById(dto.getToAccountId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "To-account not found"));

        BigDecimal amt = dto.getAmount();

        if (from.getBalance().getAmount().compareTo(amt) < 0)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Insufficient funds");

        /* movimiento */
        from.getBalance().setAmount(from.getBalance().getAmount().subtract(amt));
        to.getBalance().setAmount(to.getBalance().getAmount().add(amt));

        /* registro contable */
        txRepo.save(new Transaction(from, new Money(amt.negate()), null));
        txRepo.save(new Transaction(to,   new Money(amt),          null));
    }

    /* ═════ CAMBIO DE ESTADO ═════ */

    @Override
    @Transactional
    public void changeStatus(Long accountId, String status) {
        Account acc = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account not found"));
        acc.setStatus(Status.valueOf(status.toUpperCase()));
    }

    @Override
    @Transactional
    public void changeStatusIfOwnedByHolder(Long accountId,
                                            Long holderId,
                                            String status) {
        Account acc = getAccountIfOwnedByHolder(accountId, holderId);
        acc.setStatus(Status.valueOf(status.toUpperCase()));
    }

    /* ═════ CIERRE DE CUENTAS ═════ */

    @Override
    public void closeAccount(Long accountId) {
        if (!accountRepo.existsById(accountId))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Account not found");
        accountRepo.deleteById(accountId);
    }

    @Override
    public void closeAccountIfOwnedByHolder(Long accountId, Long holderId) {
        Account acc = getAccountIfOwnedByHolder(accountId, holderId);
        accountRepo.delete(acc);
    }
}
