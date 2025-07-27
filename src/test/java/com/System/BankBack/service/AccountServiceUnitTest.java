package com.System.BankBack.service;

import com.System.BankBack.dto.TransferDTO;
import com.System.BankBack.model.accounts.Checking;
import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.repository.AccountRepository;
import com.System.BankBack.repository.TransactionRepository;
import com.System.BankBack.service.impl.AccountServiceImpl;
import com.System.BankBack.service.FraudDetectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceUnitTest {

    @Mock  AccountRepository   accRepo;
    @Mock  TransactionRepository txRepo;
    @Mock  FraudDetectionService fraudSvc;      // ⬅️  se maqueta también
    @InjectMocks AccountServiceImpl svc;

    Checking from;  Checking to;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        // stubbing común para el fraude ─────────────
        when(fraudSvc.evaluate(any())).thenReturn(false);

        from = new Checking();  from.setId(1L);
        from.setBalance(new Money(BigDecimal.valueOf(100)));

        to   = new Checking();  to.setId(2L);
        to.setBalance(new Money(BigDecimal.ZERO));
    }

    @Test @DisplayName("transfer happy‑path mueve dinero y persiste")
    void transfer_ok() {
        when(accRepo.findById(1L)).thenReturn(Optional.of(from));
        when(accRepo.findById(2L)).thenReturn(Optional.of(to));

        TransferDTO dto = new TransferDTO();
        dto.setFromAccountId(1L);
        dto.setToAccountId(2L);
        dto.setAmount(BigDecimal.TEN);

        svc.transfer(dto);

        assertThat(from.getBalance().getAmount()).isEqualByComparingTo("90");
        assertThat(to.getBalance().getAmount()).isEqualByComparingTo("10");

        verify(accRepo, times(2)).save(any());
        verify(txRepo , times(2)).save(any());
        verify(fraudSvc, times(2)).evaluate(any());   // ⬅️  ahora cuadra
    }

    @Test @DisplayName("transfer falla si no hay fondos")
    void transfer_insufficient() {
        when(accRepo.findById(1L)).thenReturn(Optional.of(from));
        when(accRepo.findById(2L)).thenReturn(Optional.of(to));

        TransferDTO dto = new TransferDTO();
        dto.setFromAccountId(1L);
        dto.setToAccountId(2L);
        dto.setAmount(BigDecimal.valueOf(1_000));

        assertThatThrownBy(() -> svc.transfer(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Insufficient");

        verify(fraudSvc, never()).evaluate(any());    // nunca llega a llamarse
    }
}
