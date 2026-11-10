package com.example.worktrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmpResponse {
    private int id;
    private String name;
    private String email;
    private String noTelp;
    private String address;
    private LocalDate joiningDate;
    private String Status;
    private Long monthlySalary;
    private String position;

}
