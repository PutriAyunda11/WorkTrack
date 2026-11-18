package com.example.worktrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWorkScheEntyResponse{
    private int id;
    private String employeeName;
    private LocalDate workDate;
    private String scheduleType;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean workingDay;
}
