package com.example.worktrack.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id", nullable = false)
    private int id;
    private String name;
    private String email;
    private String noTelp;
    private String address;
    private LocalDate joiningDate;
    private LocalDateTime updateAt;
    private String status;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

//    @OneToOne(mappedBy = "employee")
//    private Login login;
//
//    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
//    private List<Presence> presence;
//
//    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
//    private List<LaporanCuti> laporanCuti;
//
//    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
//    private List<LaporanIzin> laporanIzin;
//
//    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
//    private List<Salary> salary;
}
