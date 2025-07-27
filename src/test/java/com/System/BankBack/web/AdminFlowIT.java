package com.System.BankBack.web;

import com.System.BankBack.dto.CreateCheckingDTO;
import com.System.BankBack.model.embedded.Money;
import com.System.BankBack.model.users.AccountHolder;
import com.System.BankBack.repository.AccountHolderRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Demuestra:
 *   1) seed de AccountHolder usando Optional (opción A)
 *   2) /​auth/login devuelve 200 + JSON con token
 *   3) el token permite crear una cuenta checking como ADMIN
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminFlowIT {

    @Autowired MockMvc               mvc;
    @Autowired ObjectMapper          om;
    @Autowired PasswordEncoder       encoder;
    @Autowired AccountHolderRepository holderRepo;

    /** se ejecuta una sola vez antes de todos los tests */
    @BeforeAll
    void seedHolder() {
        holderRepo.findByUsername("paola")
                .orElseGet(() -> {
                    AccountHolder ah = new AccountHolder();
                    ah.setUsername("paola");
                    ah.setPassword(encoder.encode("holder123"));
                    ah.setName("Paola");
                    ah.setDateOfBirth(LocalDate.of(1994,4,10));
                    ah.setPrimaryAddress(new com.System.BankBack.model.embedded.Address(
                            "Calle Luna 1","Madrid","28001","ES"));
                    ah.setMailingAddress(ah.getPrimaryAddress());
                    return holderRepo.save(ah);
                });
    }

    /** login real → devuelve “Bearer <jwt>” */
    private String jwtFor(String user, String pass) throws Exception {
        String body = mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(
                                Map.of("username", user, "password", pass))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        JsonNode node = om.readTree(body);
        assertThat(node.has("token")).isTrue();

        return "Bearer " + node.get("token").asText();
    }

    @Test
    void admin_can_create_checking_and_get_201() throws Exception {

        // 1. obtener token del admin sembrado por tu DataSeeder
        String token = jwtFor("admin", "admin123");

        // 2. payload
        CreateCheckingDTO dto = new CreateCheckingDTO();
        dto.setInitialBalance(BigDecimal.valueOf(500));
        // primaryOwnerId: el que acabamos de sembrar/asegurar
        Long holderId = holderRepo.findByUsername("paola").get().getId();
        dto.setPrimaryOwnerId(holderId);
        dto.setSecretKey("secret‑XYZ");

        // 3. petición protegida
        mvc.perform(post("/admin/checking")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.balance.amount").value(500))
                .andExpect(jsonPath("$.secretKey").value("secret‑XYZ"));
    }
}
