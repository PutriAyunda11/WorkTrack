package com.example.worktrack.enums;

public enum DayEnum {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private final String value;

    DayEnum(String value){this.value=value;}

    public String getValue() {
        return value;
    }
}
