package com.System.BankBack.model.embedded;

import jakarta.persistence.Embeddable;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Embeddable

public class Address {
    private String street, city, postalCode, country; }

