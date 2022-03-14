package com.example.project.repo;

import com.example.project.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepo extends JpaRepository<WorkoutEntity, Long> {
    List<WorkoutEntity> findAllByIsAvailableTrue();
    List<WorkoutEntity> findAllByIsAvailableFalse(); // TODO: 10.03.2022  
    WorkoutEntity findByName(String name);
    boolean existsByNameAndDurationInMinutesAndPeopleLimit(String name, int duration, int limit);
}
