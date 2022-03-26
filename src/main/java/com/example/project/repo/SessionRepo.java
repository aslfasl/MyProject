package com.example.project.repo;

import com.example.project.entity.WorkoutSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepo extends JpaRepository<WorkoutSessionEntity, Long> {
}
