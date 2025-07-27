package com.System.BankBack.service;

import com.System.BankBack.dto.ThirdPartyMovementDTO;
import com.System.BankBack.model.accounts.Checking;
import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.model.enums.Status;
import com.System.BankBack.model.users.ThirdParty;
import com.System.BankBack.repository.*;
import com.System.BankBack.service.impl.ThirdPartyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ThirdPartyServiceTest {

    @Mock ThirdPartyRepository  tpRepo;
    @Mock AccountRepository     accRepo;
    @Mock TransactionRepository txRepo;
    @Mock FraudDetectionService fraudSvc;

    @InjectMocks ThirdPartyServiceImpl svc;

    ThirdParty tp;
    Checking   acc;

    @BeforeEach void init() {
        tp = new ThirdParty();
        tp.setHashKey("key‑123");

        acc = new Checking();
        acc.setId(1L);
        acc.setSecretKey("sec");
        acc.setStatus(Status.ACTIVE);
        acc.setBalance(new Money(BigDecimal.valueOf(100)));

        given(tpRepo.findByHashKey("key‑123")).willReturn(tp);
        given(accRepo.findById(1L)).willReturn(Optional.of(acc));
    }

    @Test void send_ok() {
        ThirdPartyMovementDTO dto = new ThirdPartyMovementDTO();
        dto.setAccountId(1L);
        dto.setSecretKey("sec");
        dto.setAmount(BigDecimal.TEN);

        svc.sendMoney("key‑123", dto);

        then(txRepo).should().save(any());
        then(fraudSvc).should().evaluate(any());
    }
}