package com.example.worktrack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.worktrack.model.Login;

public interface LoginRepository extends JpaRepository<Login, Integer>{
    Optional<Login> findByUsername(String username);
    Optional<Login> findByEmployeeId(Integer id);
}
