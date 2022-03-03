package com.example.project.db.repo;

import com.example.project.db.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepo extends JpaRepository<WorkoutEntity, Long> {
}
