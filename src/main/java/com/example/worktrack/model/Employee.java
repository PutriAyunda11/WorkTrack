package com.example.worktrack.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee", indexes = {
        @Index(name = "idx_employee_name", columnList = "name"),
        @Index(name = "idx_employee_position", columnList = "position_id")
})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id", nullable = false)
    private int id;
    private String name;
    private String email;
    private String noTelp;
    private String address;
    private Long monthlySalary;
    private LocalDate joiningDate;
    private LocalDateTime updateAt;
    private String status;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

}
