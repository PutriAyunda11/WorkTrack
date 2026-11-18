package com.example.worktrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeWorkScheReq {
    private int id;
    private String positionName;
    private int employeeId;
    private LocalDate workDate;
    private String scheduleType;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean workingDay;
}
