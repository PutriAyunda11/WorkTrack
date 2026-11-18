package com.example.worktrack.enums;

public enum Status {
    ACTIVE ("Active"),
    INACTIVE ("Inactive"),
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    LATE("Late"),
    ON_TIME("On Time"),
    ABSENT("Absent"),
    OVERTIME("Overtime"),
    EARLY_LEAVE("Early Leave"),
    SICK_LEAVE("Sick Leave"),     // sakit
    ANNUAL_LEAVE("Annual Leave"),     // cuti tahunan
    OFF_DAY("Off Day");// jadwal libur

    private final String value;

    Status(String value){ this.value=value; }

    public String getValue() {
        return value;
    }
}
