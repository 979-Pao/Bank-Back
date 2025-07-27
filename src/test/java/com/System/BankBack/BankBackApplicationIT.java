package com.System.BankBack;

import com.System.BankBack.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Arranca TODO el contexto de Spring y comprueba que
 *   1) el contexto se crea sin excepciones
 *   2) un par de beans críticos están realmente registrados.
 */
@SpringBootTest
class BankBackApplicationIT {

    @Autowired
    ApplicationContext ctx;

    @Autowired
    AccountService accountService;   // bean esencial para la app

    @Test
    void contextStarts_and_coreBeansPresent() {
        // 1) el propio contexto
        assertThat(ctx).isNotNull();

        // 2) el AccountService se encuentra y está inicializado
        assertThat(accountService).isNotNull();
        // Si quieres validar que es tu implementación concreta:
        // assertThat(accountService).isInstanceOf(AccountServiceImpl.class);
    }
}
