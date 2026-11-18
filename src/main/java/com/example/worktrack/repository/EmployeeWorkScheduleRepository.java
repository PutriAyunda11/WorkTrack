package com.example.worktrack.repository;

import com.example.worktrack.model.Employee;
import com.example.worktrack.model.EmployeeWorkSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EmployeeWorkScheduleRepository extends JpaRepository<EmployeeWorkSchedule, Integer> {
    Optional<EmployeeWorkSchedule> findByWorkDateAndEmployee(LocalDate workDate, Employee emp);
    Page<EmployeeWorkSchedule> findAll(Pageable pageable);
}
