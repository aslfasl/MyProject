package com.example.project.entity;

import com.example.project.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorkoutEntityTest {

    @Test
    void shouldAddInstructorToWorkout() {
        InstructorEntity instructorEntity =
                new InstructorEntity("Jim", "Bean", "15", true,
                        LocalDate.of(2000, 1, 1));
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Jumping higher", 1, true, 5);
        assertFalse(workoutEntity.getInstructors().contains(instructorEntity));

        workoutEntity.addInstructor(instructorEntity);

        assertTrue(workoutEntity.getInstructors().contains(instructorEntity));
    }

    @Test
    void shouldThrowCustomExceptionWhenAddingInstructorInSecondTime() {
        InstructorEntity instructorEntity =
                new InstructorEntity("Jim", "Bean", "15", true,
                        LocalDate.of(2000, 1, 1));
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Jumping higher", 1, true, 5);
        workoutEntity.addInstructor(instructorEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutEntity.addInstructor(instructorEntity));

        assertEquals("Instructor " + instructorEntity.getFirstName() + " already signed for this workout",
                exception.getMessage());
    }

    @Test
    void shouldAddClientToWorkout() {
        ClientEntity clientEntity =
                new ClientEntity("Jim", "Bean", "15",
                        LocalDate.of(2000, 1, 1), true);
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Jumping higher", 1, true, 5);
        assertFalse(workoutEntity.getClients().contains(clientEntity));

        workoutEntity.addClient(clientEntity);

        assertTrue(workoutEntity.getClients().contains(clientEntity));
    }

    @Test
    void shouldThrowCustomExceptionWhenAddClientInSecondTime() {
        ClientEntity clientEntity =
                new ClientEntity("Jim", "Bean", "15",
                        LocalDate.of(2000, 1, 1), true);
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Jumping higher", 1, true, 5);
        workoutEntity.addClient(clientEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutEntity.addClient(clientEntity));

        assertEquals("Client " + clientEntity.getFirstName() + " already signed for this workout",
                exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddingClientOverTheLimitToWorkout() {
        ClientEntity clientFirst =
                new ClientEntity("White", "Horse", "145125",
                        LocalDate.of(2000, 1, 1), true);
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Jumping higher", 1, true, 1);
        workoutEntity.addClient(clientFirst);

        ClientEntity clientSecond =
                new ClientEntity("Jim", "Bean", "15",
                        LocalDate.of(2000, 1, 1), true);
        CustomException exception = assertThrows(CustomException.class,
                () -> workoutEntity.addClient(clientSecond));

        assertEquals("All free slots has been taken for this workout", exception.getMessage());
    }

    @Test
    void shouldGetActiveClientsCounter() {
        ClientEntity clientFirst =
                new ClientEntity("White", "Horse", "145125",
                        LocalDate.of(2000, 1, 1), true);
        ClientEntity clientSecond =
                new ClientEntity("Jim", "Bean", "15",
                        LocalDate.of(2000, 1, 1), false);
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Jumping higher", 1, true, 11);
        workoutEntity.addClient(clientFirst);
        workoutEntity.addClient(clientSecond);

        assertEquals(1, workoutEntity.showActiveClientsCounter());
    }
}