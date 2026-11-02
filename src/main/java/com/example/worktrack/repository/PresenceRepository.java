package com.example.worktrack.repository;

import com.example.worktrack.model.Employee;
import com.example.worktrack.model.Presence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PresenceRepository extends JpaRepository<Presence, Integer> {

    Optional<Presence> findByEmployeeAndExitTimeIsNull(Employee employee);
}
