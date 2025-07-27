package com.System.BankBack.web;

import com.System.BankBack.dto.ThirdPartyMovementDTO;       // o ThirdPartyTransferDTO según tu código
import com.System.BankBack.model.accounts.Checking;
import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.model.users.AccountHolder;
import com.System.BankBack.repository.AccountHolderRepository;
import com.System.BankBack.repository.CheckingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)   // ← permite usar @BeforeAll no‑static
class ThirdPartyControllerIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired CheckingRepository checkingRepo;
    @Autowired AccountHolderRepository holderRepo;
    @Autowired PasswordEncoder encoder;          // si tu entidad exige contraseña

    private Long accountId;                        // se rellena en el seeder

    @BeforeAll
    void seed() {
        // 1) Titular dummy
        AccountHolder owner = new AccountHolder();
        owner.setUsername("dummy");
        owner.setPassword(encoder.encode("pass"));
        owner.setName("Dummy User");
        holderRepo.save(owner);

        // 2) Cuenta con owner
        Checking acc = new Checking();
        acc.setPrimaryOwner(owner);              // <-- ¡requerido!
        acc.setBalance(new Money(BigDecimal.valueOf(1000)));
        acc.setSecretKey("sec-XYZ");
        checkingRepo.save(acc);

        accountId = acc.getId();
    }

    @Test
    void sendMoney_ok() throws Exception {         // ← declaramos Exception

        ThirdPartyMovementDTO dto = new ThirdPartyMovementDTO();
        dto.setAccountId(accountId);               // usamos la cuenta sembrada
        dto.setSecretKey("sec-XYZ");
        dto.setAmount(BigDecimal.TEN);

        mvc.perform(post("/thirdparty/refund")       // endpoint correcto
                        .header("X-Hashed-Key", "9f5a0d8e-abcdef")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }
}
