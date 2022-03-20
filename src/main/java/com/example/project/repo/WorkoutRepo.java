package com.example.project.repo;

import com.example.project.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
public interface WorkoutRepo extends JpaRepository<WorkoutEntity, Long> {

    List<WorkoutEntity> findAllByIsAvailableTrue();

    WorkoutEntity findByName(String name);

    boolean existsByNameAndDurationInMinutesAndPeopleLimit(String name, Duration duration, int limit);

    boolean existsByName(String name);

}
