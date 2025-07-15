package com.System.BankBack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Datos necesarios para crear un nuevo AccountHolder desde el endpoint /admin/holders
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountHolderDTO {

    // Credenciales de acceso
    private String username;
    private String password;

    // Datos personales
    private String name;
    private LocalDate dateOfBirth;

    // Dirección principal (obligatoria) y de correspondencia (opcional)
    private AddressDTO primaryAddress;
    private AddressDTO mailingAddress;   // puede ser null
}

