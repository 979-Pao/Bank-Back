package com.System.BankBack.service.impl;

import com.System.BankBack.dto.ThirdPartyMovementDTO;
import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.model.transactions.Transaction;
import com.System.BankBack.model.users.ThirdParty;
import com.System.BankBack.repository.AccountRepository;
import com.System.BankBack.repository.ThirdPartyRepository;
import com.System.BankBack.repository.TransactionRepository;
import com.System.BankBack.service.ThirdPartyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;        // ← faltaba

@Service
@RequiredArgsConstructor
public class ThirdPartyServiceImpl implements ThirdPartyService {

    private final ThirdPartyRepository  tpRepo;
    private final AccountRepository     accRepo;
    private final TransactionRepository txRepo;

    /* ───────────── helpers ───────────── */

    /** Valida hash-key y devuelve el ThirdParty asociado */
    private ThirdParty checkTp(String hash) {
        return Optional.ofNullable(tpRepo.findByHashKey(hash))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid third-party key"));
    }

    /** Valida que la cuenta exista y que el secretKey coincida */
    private Account checkAccount(Long id, String secret) {
        Account acc = accRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (!acc.getSecretKey().equals(secret)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Bad secret key");
        }
        return acc;
    }

    /* ───────────── SEND ───────────── */

    @Override @Transactional
    public void sendMoney(String hash, ThirdPartyMovementDTO dto) {

        ThirdParty tp  = checkTp(hash);
        Account dest   = checkAccount(dto.getAccountId(), dto.getSecretKey());
        BigDecimal amt = dto.getAmount();

        // ↑ sin métodos inc(), usamos BigDecimal directamente
        dest.getBalance().setAmount(dest.getBalance().getAmount().add(amt));

        txRepo.save(new Transaction(tp, dest, new Money(amt), null));
    }

    /* ───────────── RECEIVE ───────────── */

    @Override @Transactional
    public void receiveMoney(String hash, ThirdPartyMovementDTO dto) {

        ThirdParty tp   = checkTp(hash);
        Account origin  = checkAccount(dto.getAccountId(), dto.getSecretKey());
        BigDecimal amt  = dto.getAmount();

        if (origin.getBalance().getAmount().compareTo(amt) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        origin.getBalance().setAmount(origin.getBalance().getAmount().subtract(amt));

        txRepo.save(new Transaction(tp, origin, new Money(amt.negate()), null));
    }

    /* ───────────── LIST ───────────── */

    @Override
    public List<Transaction> listTransactions(String hashKey) {
        return txRepo.findAllByThirdPartyHashKey(hashKey);
    }

    /* ───────────── DELETE ───────────── */

    @Override
    public void deleteThirdParty(Long id) {
        if (!tpRepo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Third-party not found");
        }
        tpRepo.deleteById(id);
    }
}
