package com.example.worktrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "employee_work_schedule", indexes = {
        @Index(name = "idx_work_date", columnList = "work_date"),
        @Index(name = "idx_employee_id", columnList = "employee_id")
})
public class EmployeeWorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "work_date")
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String scheduleType;
    private boolean workingDay;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private  Employee employee;
}
