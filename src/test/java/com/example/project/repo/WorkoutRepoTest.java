package com.example.project.repo;

import com.example.project.entity.WorkoutEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorkoutRepoTest {

    @Autowired
    private WorkoutRepo workoutRepo;

    @Test
    void shouldGetAllAvailableWorkouts() {
        workoutRepo.save(new WorkoutEntity("One", 90, true));
        workoutRepo.save(new WorkoutEntity("Two", 90, true));
        workoutRepo.save(new WorkoutEntity("One", 90, false));
        workoutRepo.save(new WorkoutEntity("One", 90, false));
        System.out.println(workoutRepo.findAllByIsAvailableTrue());
        // TODO: 10.03.2022  
    }
}