package com.example.worktrack.repository;

import com.example.worktrack.model.Presence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresenceRepository extends JpaRepository<Presence, Integer> {
}
