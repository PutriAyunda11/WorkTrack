package com.example.worktrack.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Cuti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    private String tipeCuti;
    private int totalCuti;

//    @OneToMany(mappedBy = "cuti", cascade = CascadeType.ALL)
//    private List<LaporanCuti> laporanCuti;
//
//    @OneToMany(mappedBy = "cuti", cascade = CascadeType.ALL)
//    private List<LaporanIzin> laporanizin;

}
