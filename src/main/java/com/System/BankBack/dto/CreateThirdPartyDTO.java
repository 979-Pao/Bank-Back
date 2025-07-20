package com.System.BankBack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateThirdPartyDTO {
    private String username;
    private String password;
    private String hashKey;
    private String name;
}
