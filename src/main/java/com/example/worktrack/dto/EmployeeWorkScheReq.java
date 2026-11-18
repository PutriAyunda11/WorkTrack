package com.example.worktrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWorkScheReq {
    private String positionName;
    private List<EmployeeWork> employeeWorks;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeWork {
        private int employeeId;
        private LocalDate workDate;
        private String scheduleType;
        private LocalTime startTime;
        private LocalTime endTime;
    }
}
