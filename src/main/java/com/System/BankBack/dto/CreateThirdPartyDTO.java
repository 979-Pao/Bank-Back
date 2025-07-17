package com.System.BankBack.dto;

import lombok.Data;

@Data
public class CreateThirdPartyDTO {
    private String username;
    private String password;
    private String hashKey;
}