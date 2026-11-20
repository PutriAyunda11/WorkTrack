package com.example.worktrack.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddEmployeeRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone Number is required")
    private String noTelp;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Employment Status is required")
    private String employmentStatus;

    private Integer contractPeriod;

    @NotBlank(message = "Address is required")
    private Long monthlySalary;

    @NotBlank(message = "Position is required")
    private String position;
    
}
