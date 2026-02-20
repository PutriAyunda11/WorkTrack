package com.example.worktrack.enums;

public enum RoleEnum {
    ADMIN ("Admin"),
    EMPLOYEE ("Employee");

    private final String value;

    RoleEnum(String value){ this.value=value; }

    public String getValue() {
        return value;
    }
}
