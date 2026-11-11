package com.example.worktrack.repository;

import com.example.worktrack.model.EmployeeWorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeWorkScheduleRepository extends JpaRepository<EmployeeWorkSchedule, Integer> {
}
