package com.example.worktrack.model;

import java.sql.Blob;
import java.time.*;
import java.util.Date;

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
public class Presence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false )
    private int id;
    private Date datePresence;
    private String photoIn;
    private String photoOut;
    private LocalTime entryTime;
    private LocalTime exitTime;
    private String status;       //terlambat, tepat waktu,
    private long totalWorkTime;
    private String noted;       // noted kalo lembur
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
