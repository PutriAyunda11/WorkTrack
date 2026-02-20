package com.example.worktrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.worktrack.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
    Role findRoleByName(String name);
}
