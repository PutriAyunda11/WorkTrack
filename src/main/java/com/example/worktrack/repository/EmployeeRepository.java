package com.example.worktrack.repository;

import java.util.List;
import java.util.Optional;

import com.example.worktrack.dto.UpdateEmpResponse;
import com.example.worktrack.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.worktrack.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer>{
    Optional<Employee> findById(int id);
    Optional<Employee> findByEmail(String email);
    Employee findByNameAndPosition(String name, Position position);
}

