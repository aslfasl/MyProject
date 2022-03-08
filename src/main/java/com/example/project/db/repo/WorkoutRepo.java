package com.example.project.db.repo;

import com.example.project.db.entity.ClientEntity;
import com.example.project.db.entity.InstructorEntity;
import com.example.project.db.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface WorkoutRepo extends JpaRepository<WorkoutEntity, Long> {

}
