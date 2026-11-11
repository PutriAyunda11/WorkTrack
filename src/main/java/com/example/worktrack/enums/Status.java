package com.example.worktrack.enums;

public enum Status {
    ACTIVE ("Active"),
    INACTIVE ("Inactive"),
    LATE("Late"),
    ON_TIME("On Time");

    private final String value;

    Status(String value){ this.value=value; }

    public String getValue() {
        return value;
    }
}
