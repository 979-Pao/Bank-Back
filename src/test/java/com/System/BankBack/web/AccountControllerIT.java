package com.System.BankBack.web;

import com.System.BankBack.dto.TransferDTO;
import com.System.BankBack.model.accounts.Checking;
import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.model.enums.RoleType;
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
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountControllerIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired AccountHolderRepository holderRepo;
    @Autowired CheckingRepository checkingRepo;
    @Autowired PasswordEncoder encoder;

    Long fromId, toId;

    @BeforeAll
    void seed() {
        // 1) usuario único
        AccountHolder paola = holderRepo.findByUsername("paola")
                .orElseGet(() -> {
                    AccountHolder ah = new AccountHolder();
                    ah.setUsername("paola");
                    ah.setPassword(encoder.encode("holder123"));
                    ah.setName("Paola");
                    return holderRepo.save(ah);
                });

        // 2) dos cuentas
        fromId = ensureChecking(paola, BigDecimal.valueOf(500), "sec-FROM");
        toId   = ensureChecking(paola, BigDecimal.valueOf(100), "sec-TO");
    }

    private Long ensureChecking(AccountHolder owner, BigDecimal balance, String secret){
        return checkingRepo.findAll().stream()
                .filter(c -> secret.equals(c.getSecretKey()))
                .map(Checking::getId)
                .findFirst()
                .orElseGet(() -> {
                    Checking c = new Checking();
                    c.setBalance(new Money(balance));
                    c.setPrimaryOwner(owner);
                    c.setSecretKey(secret);
                    return checkingRepo.save(c).getId();
                });
    }

    private String jwtFor(String user, String pass) throws Exception {
        String body = om.writeValueAsString(Map.of("username", user, "password", pass));

        String json = mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return "Bearer " + om.readTree(json).get("token").asText();
    }

    @Test
    void transfer_success() throws Exception {
        String token = jwtFor("paola", "holder123");

        TransferDTO dto = new TransferDTO();
        dto.setFromAccountId(fromId);
        dto.setToAccountId(toId);
        dto.setAmount(BigDecimal.ONE);

        mvc.perform(post("/holder/transfer")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isNoContent());   // 204
    }
}