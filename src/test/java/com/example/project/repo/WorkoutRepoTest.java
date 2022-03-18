package com.example.project.repo;

import com.example.project.entity.WorkoutEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorkoutRepoTest {

    @Autowired
    private WorkoutRepo workoutRepo;

    @Test
    void shouldGetAllAvailableWorkouts() {
        workoutRepo.save(new WorkoutEntity("One", Duration.ofMinutes(90), true, 100));
        workoutRepo.save(new WorkoutEntity("Two", Duration.ofMinutes(90), true, 100));
        workoutRepo.save(new WorkoutEntity("Three", Duration.ofMinutes(90), false, 100));

        assertEquals(2, workoutRepo.findAllByIsAvailableTrue().size());
        assertTrue(workoutRepo.findAllByIsAvailableTrue().stream().allMatch(WorkoutEntity::isAvailable));
    }
}