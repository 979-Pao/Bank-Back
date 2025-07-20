package com.System.BankBack.model.enums;

public enum RoleType {
    ADMIN,
    ACCOUNTHOLDER,
    THIRD_PARTY;

    /** Formato que Spring Security espera: ROLE_XYZ */
    public String asAuthority() {
        return "ROLE_" + this.name();          // p.e. ROLE_ADMIN
    }
}
