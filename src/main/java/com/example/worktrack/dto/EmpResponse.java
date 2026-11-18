package com.example.worktrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmpResponse {
    private int id;
    private String name;
    private String email;
    private String noTelp;
    private String address;
    private LocalDate joiningDate;
    private String status;
    private Long monthlySalary;
    private String position;

}
