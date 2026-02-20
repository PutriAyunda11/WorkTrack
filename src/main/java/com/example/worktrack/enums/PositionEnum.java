package com.example.worktrack.enums;

public enum PositionEnum {
    ADMIN_HRD ("Admin HRD"),
    OPERATOR_PRODUKSI ("Operator Produksi"),
    HELPER_PRODUKSI ("Helper Produksi"),
    TEKNISI_MESIN ("Teknisi Mesin"),
    QUALITY_CONTROL ("Quality Control"),
    STAF_GUDANG ("Staf Gudang"),
    ADMIN_GUDANG ("Admin Gudang"),
    CLEANING_SERVICE ("Cleaning Service"),
    SECURITY ("Security"),
    HR_SUPERVISIOR ("HR Supervisior");

    private final String value;

    PositionEnum(String value){ this.value=value; }

    public String getValue() {
        return value;
    }
}
