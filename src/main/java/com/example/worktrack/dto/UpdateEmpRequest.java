package com.example.worktrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmpRequest {
    private int id;
    private String name;
    private String email;
    private String noTelp;
    private String address;
    private String position;
}
