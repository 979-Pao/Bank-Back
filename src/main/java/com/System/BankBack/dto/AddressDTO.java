package com.System.BankBack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                 // getters / setters / toString / equals-hash
@NoArgsConstructor    // ctor vacío
@AllArgsConstructor   // ctor con todos los campos
public class AddressDTO {

    private String street;
    private String city;
    private String postalCode;
    private String country;
}
