package com.example.worktrack.enums;

public enum CutiEnum {
    CUTI_TAHUNAN ("Cuti Tahunan"),
    CUTI_SAKIT ("Cuti Sakit"),
    CUTI_MELAHIRKAN ("Cuti Melahirkan"),
    CUTI_MENIKAH ("Cuti Menikah"),
    CUTI_KEPENTINGAN_KELUARGA ("Cuti Kepentingan Keluarga"),
    CUTI_UMROH ("Cuti Umroh");

    private final String value;

    CutiEnum(String value){ this.value=value; }

    public String getValue() {
        return value;
    }
}
