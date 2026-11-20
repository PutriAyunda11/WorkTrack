package com.example.worktrack.repository;

import java.util.List;
import java.util.Optional;

import com.example.worktrack.model.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.worktrack.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer>{
    Optional<Employee> findById(int id);
    Optional<Employee> findByEmail(String email);
    Employee findByNameAndPosition(String name, Position position);
    List<Employee> findByPosition_PositionName(String position);
    Page<Employee> findAllByStatus(String status, Pageable pageable);
    Optional<Employee> findByIdAndStatus(Integer id, String status);
}

