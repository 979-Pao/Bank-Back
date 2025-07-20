package com.System.BankBack.service.impl;

import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.enums.Status;
import com.System.BankBack.model.transactions.Transaction;
import com.System.BankBack.repository.AccountRepository;
import com.System.BankBack.repository.TransactionRepository;
import com.System.BankBack.service.FraudDetectionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private static final Logger log = LoggerFactory.getLogger(FraudDetectionServiceImpl.class);

    private final TransactionRepository txRepo;
    private final AccountRepository     accRepo;

    /* ───────────────────────────── evaluate ─────────────────────────── */

    @Override
    public boolean evaluate(Transaction tx) {

        Account acc = tx.getAccount();        // origen o destino
        boolean frozen = false;

        /* ① Volumen diario > 150 % del récord histórico */
        if (exceedsDailyRecord(acc.getId())) {
            frozen = true;
        }

        /* ② ≥ 3 transacciones en < 1 s */
        if (threeInOneSecond(acc.getId(), tx.getDateTime())) {
            frozen = true;
        }

        /* Congelar si procede y avisar en logs */
        if (frozen && acc.getStatus() != Status.FROZEN) {
            acc.setStatus(Status.FROZEN);
            accRepo.save(acc);

            log.warn("⚠️ FRAUDE – cuenta {} congelada", acc.getId());
        }
        return frozen;
    }

    /* ─────────────────────────── helpers ───────────────────────────── */

    /** ¿Transacciones de hoy superan el 150 % del mayor total diario pasado? */
    private boolean exceedsDailyRecord(Long accId) {

        LocalDate today = LocalDate.now();

        BigDecimal totalToday     = txRepo.sumAmountByAccountAndDate(accId, today);
        BigDecimal historicalMax  = txRepo.maxDailyTotalBeforeDate(accId, today);

        // si todavía no hay histórico, no aplica
        if (historicalMax == null) return false;

        return totalToday.compareTo(historicalMax.multiply(BigDecimal.valueOf(1.5))) > 0;
    }

    /** ¿Ya existen ≥ 2 operaciones en la ventana [t – 1 s, t] ?  */
    private boolean threeInOneSecond(Long accId, LocalDateTime t) {

        LocalDateTime from = t.minusSeconds(1);
        long count = txRepo.countByAccountAndTimeWindow(accId, from, t);
        return count >= 2;
    }
}
