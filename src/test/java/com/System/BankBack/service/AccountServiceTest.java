package com.System.BankBack.service;

import com.System.BankBack.dto.TransferDTO;
import com.System.BankBack.model.accounts.Checking;
import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.model.enums.Status;
import com.System.BankBack.repository.*;
import com.System.BankBack.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountServiceTest {

    @Mock AccountRepository       accRepo;
    @Mock TransactionRepository   txRepo;
    @Mock AccountHolderRepository holderRepo;
    @Mock FraudDetectionService   fraudSvc;

    @InjectMocks AccountServiceImpl svc;

    Checking from, to;

    @BeforeEach void setUp() {
        from = new Checking();
        from.setId(1L);
        from.setStatus(Status.ACTIVE);
        from.setBalance(new Money(BigDecimal.valueOf(500)));

        to = new Checking();
        to.setId(2L);
        to.setStatus(Status.ACTIVE);
        to.setBalance(new Money(BigDecimal.ZERO));

        when(accRepo.findById(1L)).thenReturn(Optional.of(from));
        when(accRepo.findById(2L)).thenReturn(Optional.of(to));
        when(accRepo.findById(anyLong()))
                .thenAnswer(inv -> Optional.of(inv.getArgument(0).equals(1L) ? from : to));
    }

    @Test void transfer_ok() {
        TransferDTO dto = new TransferDTO();
        dto.setFromAccountId(1L);
        dto.setToAccountId(2L);
        dto.setAmount(BigDecimal.valueOf(100));

        svc.transfer(dto);

        verify(accRepo, times(2)).save(any(Checking.class));
        verify(txRepo, times(2)).save(any());
        verify(fraudSvc, times(2)).evaluate(any());
    }

    @Test void transfer_frozenRejected() {
        from.setStatus(Status.FROZEN);

        TransferDTO dto = new TransferDTO();
        dto.setFromAccountId(1L);
        dto.setToAccountId(2L);
        dto.setAmount(BigDecimal.TEN);

        assertThatThrownBy(() -> svc.transfer(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("frozen");
    }
}