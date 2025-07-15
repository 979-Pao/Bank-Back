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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThirdPartyServiceImpl implements ThirdPartyService {

    private final ThirdPartyRepository tpRepo;
    private final AccountRepository accRepo;
    private final TransactionRepository txRepo;

    /* ---------- POST send money ---------- */
    @Override
    @Transactional
    public void sendMoney(String hashedKey, ThirdPartyMovementDTO dto) {

        ThirdParty tp = tpRepo.findByHashedKey(hashedKey);
        if (tp == null) throw new RuntimeException("Invalid third-party key");

        Account dest = accRepo.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (!dest.getSecretKey().equals(dto.getSecretKey()))
            throw new RuntimeException("Bad secret key");

        BigDecimal amount = dto.getAmount();

        dest.getBalance().setAmount(dest.getBalance().getAmount().add(amount));
        txRepo.save(new Transaction(tp, dest, new Money(amount), null));
    }

    /* ---------- POST receive money ---------- */
    @Override
    @Transactional
    public void receiveMoney(String hashedKey, ThirdPartyMovementDTO dto) {

        ThirdParty tp = tpRepo.findByHashedKey(hashedKey);
        if (tp == null) throw new RuntimeException("Invalid third-party key");

        Account origin = accRepo.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Origin account not found"));

        if (!origin.getSecretKey().equals(dto.getSecretKey()))
            throw new RuntimeException("Bad secret key");

        BigDecimal amount = dto.getAmount();

        if (origin.getBalance().getAmount().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient funds");

        origin.getBalance().setAmount(origin.getBalance().getAmount().subtract(amount));
        txRepo.save(new Transaction(tp, origin, new Money(amount.negate()), null));
    }

    /* ---------- GET own transactions ---------- */
    @Override
    public List<Transaction> listTransactions(String hashedKey) {
        return txRepo.findAllByThirdPartyHashedKey(hashedKey);
    }

    /* ---------- DELETE third-party ---------- */
    @Override
    public void deleteThirdParty(Long id) {
        if (!tpRepo.existsById(id))
            throw new RuntimeException("Third-party not found");
        tpRepo.deleteById(id);
    }
}
