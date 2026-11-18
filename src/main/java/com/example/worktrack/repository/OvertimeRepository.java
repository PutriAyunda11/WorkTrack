package com.example.worktrack.repository;

import com.example.worktrack.model.Employee;
import com.example.worktrack.model.Overtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OvertimeRepository extends JpaRepository<Overtime, Integer> {
    Overtime findByOvertimeDateAndEmployee(LocalDate overtimeDate, Employee employee);
}
