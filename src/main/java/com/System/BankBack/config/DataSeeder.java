package com.System.BankBack.config;

import com.System.BankBack.model.embedded.Address;
import com.System.BankBack.model.users.*;
import com.System.BankBack.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("admin")                     // se carga solo con spring.profiles.active=admin
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AdminRepository adminRepo;
    private final AccountHolderRepository holderRepo;
    private final ThirdPartyRepository tpRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {

        // Evita duplicar datos si ya hay registros en account_holder
        if (holderRepo.count() > 0) return;

        /* -------- ADMIN -------- */
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword(encoder.encode("admin123"));
        admin.setName("Super Admin");
        adminRepo.save(admin);

        /* -------- ACCOUNTHOLDER -------- */
        AccountHolder holder = new AccountHolder();
        holder.setUsername("paola");
        holder.setPassword(encoder.encode("holder123"));
        holder.setName("Paola");
        holder.setDateOfBirth(LocalDate.of(1995, 4, 10));

        Address addr = new Address("Calle Luna 1", "Madrid", "28001", "ES");
        holder.setPrimaryAddress(addr);           // ¡obligatorio!
        holder.setMailingAddress(addr);           // opcional; reutilizamos la misma

        holderRepo.save(holder);                  // <— esto poblará la tabla account_holder

        /* -------- THIRD PARTY -------- */
        ThirdParty gateway = new ThirdParty();
        gateway.setUsername("gateway");           // opcional para login
        gateway.setPassword(encoder.encode("third123"));
        gateway.setName("Gateway Service");
        gateway.setHashedKey("9f5a0d8e-abcdef");  // pon tu hash real
        tpRepo.save(gateway);

        System.out.println("🌱 Seed insertado: admin, paola, gateway");
    }
}
