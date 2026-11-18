package com.example.worktrack.repository;

import com.example.worktrack.model.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Integer> {
    Optional<WorkSchedule> findByNamePositionAndDay(String namePosition, String day);
}
