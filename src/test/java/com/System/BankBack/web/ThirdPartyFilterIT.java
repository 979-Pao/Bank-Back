package com.System.BankBack.web;

import com.System.BankBack.dto.TransferDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ThirdPartyFilterIT {
    @Autowired MockMvc mvc; @Autowired ObjectMapper om;
    @Test void badKey_returns401() throws Exception {
        TransferDTO dto = new TransferDTO();
        dto.setToAccountId(999L); dto.setAmount(BigDecimal.ONE); dto.setSecretKey("doesnt‑care");

        mvc.perform(post("/thirdparty/refund")
                        .header("X-Hashed-Key","BAD‑KEY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }
}