package com.System.BankBack.repo;

import com.System.BankBack.model.users.AccountHolder;
import com.System.BankBack.repository.AccountHolderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest          // ya trae transacción + rollback
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoriesIT {

    @Autowired AccountHolderRepository repo;

    @Test
    void findByUsername_ok() {
        AccountHolder h = new AccountHolder();
        h.setUsername("john");
        h.setPassword("x");
        h.setName("John");
        repo.save(h);

        assertThat(repo.findByUsername("john")).isPresent();
    }
}