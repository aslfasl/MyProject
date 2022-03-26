package com.example.project.repo;

import com.example.project.entity.WorkoutClassEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClassRepoTest {
//
//    @Autowired
//    private ClassRepo classRepo;
//
//    @Test
//    void shouldGetAllAvailableWorkouts() {
//        classRepo.save(new WorkoutClassEntity("One", Duration.ofMinutes(90), true, 100));
//        classRepo.save(new WorkoutClassEntity("Two", Duration.ofMinutes(90), true, 100));
//        classRepo.save(new WorkoutClassEntity("Three", Duration.ofMinutes(90), false, 100));
//
//        assertEquals(2, classRepo.findAllByIsAvailableTrue().size());
//        assertTrue(classRepo.findAllByIsAvailableTrue().stream().allMatch(WorkoutClassEntity::isAvailable));
//    }
}