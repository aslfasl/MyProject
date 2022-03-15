package com.example.project.entity;

import com.example.project.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientEntityTest {

    @Test
    void shouldAddWorkoutToClientEntity() {
        ClientEntity clientEntity =
                new ClientEntity("Jack", "Dogson", "890123",
                        LocalDate.of(1989, 1,1), true);
        WorkoutEntity workoutEntity = new WorkoutEntity("circle running", 999, true, 100);
        assertFalse(clientEntity.getClientWorkouts().contains(workoutEntity));

        clientEntity.addWorkout(workoutEntity);

        assertEquals(1, clientEntity.getClientWorkouts().size());
        assertTrue(clientEntity.getClientWorkouts().contains(workoutEntity));
    }

    @Test
    void shouldThrowCustomExceptionWhenAddWorkoutInSecondTime() {
        ClientEntity clientEntity =
                new ClientEntity("Jack", "Dogson", "890123",
                        LocalDate.of(1989, 1,1), true);
        WorkoutEntity workoutEntity = new WorkoutEntity("circle running", 999, true, 100);
        clientEntity.addWorkout(workoutEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> clientEntity.addWorkout(workoutEntity));

        assertEquals("This client already signed for: " + workoutEntity.getName(), exception.getMessage());
    }
}