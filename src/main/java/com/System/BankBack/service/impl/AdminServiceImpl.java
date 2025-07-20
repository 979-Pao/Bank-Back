package com.System.BankBack.service.impl;

import com.System.BankBack.dto.*;
import com.System.BankBack.model.accounts.*;
import com.System.BankBack.model.embedded.Address;
import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.model.users.AccountHolder;
import com.System.BankBack.model.users.ThirdParty;
import com.System.BankBack.model.users.User;
import com.System.BankBack.repository.*;
import com.System.BankBack.service.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    /* ---------- Repos ---------- */
    private final AccountHolderRepository   holderRepo;
    private final UserRepository            userRepo;
    private final AccountRepository         accountRepo;
    private final ThirdPartyRepository      thirdPartyRepo;
    private final CheckingRepository        checkingRepo;
    private final StudentCheckingRepository studentRepo;
    private final SavingsRepository         savingsRepo;
    private final CreditCardRepository      creditRepo;

    /* ---------- Utils ---------- */
    private final PasswordEncoder passwordEncoder;

    /* ═══════ Helpers ═══════ */
    /** Devuelve la clave recibida o genera una UUID si falta/viene vacía */
    private String ensureKey(String candidate) {
        return (candidate == null || candidate.isBlank())
                ? UUID.randomUUID().toString()
                : candidate;
    }

    private void setSecondaryIfPresent(Account acc, Long secondaryId) {
        if (secondaryId != null)
            holderRepo.findById(secondaryId).ifPresent(acc::setSecondaryOwner);
    }

    /* ───── CREATE CHECKING / STUDENT-CHECKING ───── */
    @Override
    public Account createChecking(CreateCheckingDTO dto) {

        AccountHolder primary = holderRepo.findById(dto.getPrimaryOwnerId())
                .orElseThrow(() -> new RuntimeException("Primary owner not found"));

        int age = Period.between(primary.getDateOfBirth(), LocalDate.now()).getYears();
        Money balance = new Money(dto.getInitialBalance());

        if (age < 24) {                    // StudentChecking
            StudentChecking sc = new StudentChecking();
            sc.setBalance(balance);
            sc.setPrimaryOwner(primary);
            sc.setSecretKey(ensureKey(dto.getSecretKey()));
            setSecondaryIfPresent(sc, dto.getSecondaryOwnerId());
            return studentRepo.save(sc);
        }

        Checking ck = new Checking();      // Checking
        ck.setBalance(balance);
        ck.setPrimaryOwner(primary);
        ck.setSecretKey(ensureKey(dto.getSecretKey()));
        setSecondaryIfPresent(ck, dto.getSecondaryOwnerId());
        return checkingRepo.save(ck);
    }

    /* ───── CREATE SAVINGS ───── */
    @Override
    public Account createSavings(CreateSavingsDTO dto) {

        AccountHolder owner = holderRepo.findById(dto.getPrimaryOwnerId())
                .orElseThrow(() -> new RuntimeException("Primary owner not found"));

        Savings sav = new Savings();
        sav.setBalance(new Money(dto.getInitialBalance()));
        sav.setPrimaryOwner(owner);
        sav.setSecretKey(ensureKey(dto.getSecretKey()));

        if (dto.getInterestRate()   != null) sav.setInterestRate(dto.getInterestRate());
        if (dto.getMinimumBalance() != null) sav.setMinimumBalance(dto.getMinimumBalance());
        setSecondaryIfPresent(sav, dto.getSecondaryOwnerId());

        return savingsRepo.save(sav);
    }

    /* ───── CREATE CREDIT CARD ───── */
    @Override
    public Account createCreditCard(CreateCreditCardDTO dto) {

        AccountHolder owner = holderRepo.findById(dto.getPrimaryOwnerId())
                .orElseThrow(() -> new RuntimeException("Primary owner not found"));

        CreditCard cc = new CreditCard();
        cc.setBalance(new Money(dto.getInitialBalance()));
        cc.setPrimaryOwner(owner);

        if (dto.getCreditLimit()   != null) cc.setCreditLimit(dto.getCreditLimit());
        if (dto.getInterestRate()  != null) cc.setInterestRate(dto.getInterestRate());
        setSecondaryIfPresent(cc, dto.getSecondaryOwnerId());

        return creditRepo.save(cc);
    }

    /* ───── CREATE ACCOUNT HOLDER ───── */
    @Override
    public AccountHolder createAccountHolder(CreateAccountHolderDTO dto) {

        if (userRepo.existsByUsername(dto.getUsername()))
            throw new RuntimeException("Username already taken");

        AccountHolder holder = new AccountHolder();
        holder.setUsername(dto.getUsername());
        holder.setPassword(passwordEncoder.encode(dto.getPassword()));
        holder.setName(dto.getName());
        holder.setDateOfBirth(dto.getDateOfBirth());
        holder.setPrimaryAddress(toAddress(dto.getPrimaryAddress()));

        if (dto.getMailingAddress() != null)
            holder.setMailingAddress(toAddress(dto.getMailingAddress()));

        return holderRepo.save(holder);
    }

    /* ───── CREATE THIRDPARTY ───── */
    @Override
    public ThirdParty createThirdParty(CreateThirdPartyDTO dto) {
        if (userRepo.existsByUsername(dto.getUsername()))
            throw new RuntimeException("Username already taken");

        ThirdParty tp = new ThirdParty();
        tp.setUsername(dto.getUsername());
        tp.setPassword(passwordEncoder.encode(dto.getPassword()));
        tp.setName(dto.getName()); //

        // Si no viene el hashKey o está vacío, generamos uno automáticamente
        String safeHashKey = (dto.getHashKey() == null || dto.getHashKey().isBlank())
                ? "TP-" + UUID.randomUUID()
                : dto.getHashKey();

        tp.setHashKey(safeHashKey);

        return thirdPartyRepo.save(tp);
    }


    /* ───── LISTAR ───── */
    @Override public List<AccountHolder> findAllHolders()  { return holderRepo.findAll(); }
    @Override public List<Account>       findAllAccounts() { return accountRepo.findAll(); }
    @Override public List<ThirdParty> findAllThirdParties() {return thirdPartyRepo.findAll();}
    @Override public List<User> findAllUsers() { return userRepo.findAll();}

    /* ───── UPDATE BALANCE ───── */
    @Override
    @Transactional
    public Account updateBalance(Long id, UpdateBalanceDTO dto) {

        Account acc = accountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        acc.getBalance().setAmount(dto.getNewBalance());
        return acc;               // se persiste al finalizar la transacción
    }

    /* ───── DELETE ACCOUNT ───── */
    @Override
    public void deleteAccount(Long id) {
        if (!accountRepo.existsById(id))
            throw new RuntimeException("Account not found");
        accountRepo.deleteById(id);
    }

    /* ═════════ Helpers ═════════ */
    private Address toAddress(AddressDTO dto) {
        Address a = new Address();
        a.setStreet(dto.getStreet());
        a.setCity(dto.getCity());
        a.setPostalCode(dto.getPostalCode());
        a.setCountry(dto.getCountry());
        return a;
    }
}
