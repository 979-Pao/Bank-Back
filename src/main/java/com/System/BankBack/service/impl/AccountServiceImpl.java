package com.System.BankBack.service.impl;

import com.System.BankBack.dto.TransferDTO;
import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.model.enums.Status;
import com.System.BankBack.model.transactions.Transaction;
import com.System.BankBack.repository.AccountRepository;
import com.System.BankBack.repository.TransactionRepository;
import com.System.BankBack.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepo;
    private final TransactionRepository txRepo;

    /* ---------- GET balance ---------- */
    @Override
    public Money getBalance(Long id) {
        return accountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"))
                .getBalance();
    }

    /* ---------- POST transfer ---------- */
    @Override
    @Transactional
    public void transfer(TransferDTO dto) {

        Account from = accountRepo.findById(dto.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("From-account not found"));

        Account to = accountRepo.findById(dto.getToAccountId())
                .orElseThrow(() -> new RuntimeException("To-account not found"));

        BigDecimal amt = dto.getAmount();

        if (from.getBalance().getAmount().compareTo(amt) < 0)
            throw new RuntimeException("Insufficient funds");

        /* movimiento */
        from.getBalance().setAmount(from.getBalance().getAmount().subtract(amt));
        to.getBalance().setAmount(to.getBalance().getAmount().add(amt));

        /* persistencia */
        accountRepo.save(from);
        accountRepo.save(to);

        /* registro contable */
        txRepo.save(new Transaction(from, new Money(amt.negate()), null));
        txRepo.save(new Transaction(to,   new Money(amt),          null));
    }

    /* ---------- PATCH status ---------- */
    @Override
    @Transactional
    public void changeStatus(Long id, String status) {
        Account acc = accountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        acc.setStatus(Status.valueOf(status.toUpperCase()));
    }

    /* ---------- DELETE close account ---------- */
    @Override
    public void closeAccount(Long id) {
        if (!accountRepo.existsById(id))
            throw new RuntimeException("Account not found");
        accountRepo.deleteById(id);
    }
}
