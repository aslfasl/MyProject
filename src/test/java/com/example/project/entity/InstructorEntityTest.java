package com.example.project.entity;

import com.example.project.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InstructorEntityTest {

    @Test
    void shouldAddWorkoutToInstructorEntity() {
        InstructorEntity instructorEntity =
                new InstructorEntity("Jack", "Dogson", "890123",
                        LocalDate.of(1989, 1, 1), true);
        WorkoutEntity workoutEntity = new WorkoutEntity("circle running", 999, true, 100);
        assertFalse(instructorEntity.getInstructorWorkouts().contains(workoutEntity));

        instructorEntity.addWorkout(workoutEntity);

        assertTrue(instructorEntity.getInstructorWorkouts().contains(workoutEntity));
    }

    @Test
    void shouldThrowCustomExceptionWhenAddWorkoutInSecondTime() {
        InstructorEntity instructorEntity =
                new InstructorEntity("Jack", "Dogson", "890123",
                        LocalDate.of(1989, 1, 1), true);
        assertEquals(0, instructorEntity.getInstructorWorkouts().size());
        WorkoutEntity workoutEntity = new WorkoutEntity("circle running", 999, true, 100);
        instructorEntity.addWorkout(workoutEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> instructorEntity.addWorkout(workoutEntity));

        assertEquals("This instructor already signed for: " + workoutEntity.getName(), exception.getMessage());
    }
}