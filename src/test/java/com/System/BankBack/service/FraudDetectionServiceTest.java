package com.System.BankBack.service;

import com.System.BankBack.model.accounts.Checking;
import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.model.enums.Status;
import com.System.BankBack.model.transactions.Transaction;
import com.System.BankBack.repository.AccountRepository;
import com.System.BankBack.repository.TransactionRepository;
import com.System.BankBack.service.impl.FraudDetectionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FraudDetectionServiceTest {

    @Mock  TransactionRepository txRepo;
    @Mock  AccountRepository     accRepo;

    @InjectMocks
    FraudDetectionServiceImpl fraudSvc;

    Checking acc;                   // ← clase concreta

    @BeforeEach void init() {
        acc = new Checking();
        acc.setId(1L);
        acc.setStatus(Status.ACTIVE);
    }

    @Test @DisplayName("❌ congela si hoy > 150 % récord histórico")
    void freezeOnDailySpike() {
        Transaction tx = new Transaction();
        tx.setAccount(acc);
        tx.setDateTime(LocalDateTime.now());
        tx.setAmount(new Money(BigDecimal.valueOf(5000)));

        LocalDate today = LocalDate.now();
        given(txRepo.sumAmountByAccountAndDate(1L, today))
                .willReturn(BigDecimal.valueOf(10000));
        given(txRepo.maxDailyTotalBeforeDate(1L, today))
                .willReturn(BigDecimal.valueOf(5000));

        boolean fraud = fraudSvc.evaluate(tx);

        assertThat(fraud).isTrue();
        assertThat(acc.getStatus()).isEqualTo(Status.FROZEN);
    }

    @Test @DisplayName("❌ congela con 3 movs en <1 s")
    void freezeOnBurst() {
        LocalDateTime now = LocalDateTime.now();
        Transaction tx = new Transaction(acc, new Money(BigDecimal.TEN), now);

        given(txRepo.countByAccountAndTimeWindow(1L, now.minusSeconds(1), now))
                .willReturn(2L);

        boolean fraud = fraudSvc.evaluate(tx);

        assertThat(fraud).isTrue();
        assertThat(acc.getStatus()).isEqualTo(Status.FROZEN);
    }

    @Test @DisplayName("✅ no congela en caso normal")
    void safeWhenNormal() {
        LocalDateTime now = LocalDateTime.now();
        Transaction tx = new Transaction(acc, new Money(BigDecimal.TEN), now);

        given(txRepo.countByAccountAndTimeWindow(1L, now.minusSeconds(1), now))
                .willReturn(0L);
        given(txRepo.sumAmountByAccountAndDate(Mockito.eq(1L), Mockito.any()))
                .willReturn(BigDecimal.valueOf(100));
        given(txRepo.maxDailyTotalBeforeDate(Mockito.eq(1L), Mockito.any()))
                .willReturn(BigDecimal.valueOf(500));

        boolean fraud = fraudSvc.evaluate(tx);

        assertThat(fraud).isFalse();
        assertThat(acc.getStatus()).isEqualTo(Status.ACTIVE);
    }
}