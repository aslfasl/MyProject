package com.example.project.repo;

import com.example.project.entity.WorkoutClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutClassRepo extends JpaRepository<WorkoutClassEntity, Long> {

    List<WorkoutClassEntity> findAllByIsAvailableTrue();

    WorkoutClassEntity findByName(String name);

    boolean existsByName(String name);

}
