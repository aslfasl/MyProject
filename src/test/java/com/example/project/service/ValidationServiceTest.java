package com.example.project.service;

import com.example.project.converter.Converter;
import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.exception.CustomException;
import com.example.project.repo.ClientRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;

import static com.example.project.exception.ExceptionMessageUtils.WRONG_AGE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidationServiceTest {

    @Autowired
    ValidationService validationService;

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    Converter converter;

    @Test
    void shouldThrowCustomExceptionWhenCheckClientStatus() {
        ClientDto clientDto =
                new ClientDto("Vag", "Prl", "asdf", LocalDate.of(1000, 1, 1), false, new HashSet<>());

        CustomException exception =
                assertThrows(CustomException.class, () -> validationService.checkClientStatus(clientDto));

        assertEquals("Client is not active", exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenCheckInstructorStatus() {
        InstructorDto instructorDto =
                new InstructorDto("Vag", "Prl", "asdf", false, LocalDate.of(1000, 1, 1), new HashSet<>());

        CustomException exception =
                assertThrows(CustomException.class, () -> validationService.checkInstructorStatus(instructorDto));

        assertEquals("Instructor is not active", exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenCheckIsWorkoutAvailable() {
        WorkoutEntity workoutEntity = new WorkoutEntity("Workout 1", Duration.ofMinutes(55), false, 15);

        CustomException exception =
                assertThrows(CustomException.class, () -> validationService.checkIsWorkoutAvailable(workoutEntity));

        assertEquals("Workout is not available", exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenCheckClientAge() {
        ClientDto clientDto1 =
                new ClientDto("Vag", "Prl", "asdf", LocalDate.of(1000, 1, 1), true, new HashSet<>());
        ClientDto clientDto2 =
                new ClientDto("Vag", "Prl", "asdf", LocalDate.of(2100, 1, 1), true, new HashSet<>());
        ClientDto clientDto3 =
                new ClientDto("Vag", "Prl", "asdf", LocalDate.of(2021, 1, 1), true, new HashSet<>());

        CustomException exception1 =
                assertThrows(CustomException.class, () -> validationService.checkClientAge(clientDto1));
        CustomException exception2 =
                assertThrows(CustomException.class, () -> validationService.checkClientAge(clientDto2));
        CustomException exception3 =
                assertThrows(CustomException.class, () -> validationService.checkClientAge(clientDto3));

        assertEquals(WRONG_AGE, exception1.getMessage());
        assertEquals(WRONG_AGE, exception2.getMessage());
        assertEquals(WRONG_AGE, exception3.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenCheckInstructorAge() {
        InstructorDto instructorDto1 =
                new InstructorDto("Vag", "Prl", "asdf", true, LocalDate.of(1000, 1, 1), new HashSet<>());
        InstructorDto instructorDto2 =
                new InstructorDto("Vag", "Prl", "asdf", true, LocalDate.of(2100, 1, 1), new HashSet<>());
        InstructorDto instructorDto3 =
                new InstructorDto("Vag", "Prl", "asdf", true, LocalDate.of(2021, 1, 1), new HashSet<>());

        CustomException exception1 =
                assertThrows(CustomException.class, () -> validationService.checkInstructorAge(instructorDto1));
        CustomException exception2 =
                assertThrows(CustomException.class, () -> validationService.checkInstructorAge(instructorDto2));
        CustomException exception3 =
                assertThrows(CustomException.class, () -> validationService.checkInstructorAge(instructorDto3));

        assertEquals(WRONG_AGE, exception1.getMessage());
        assertEquals(WRONG_AGE, exception2.getMessage());
        assertEquals(WRONG_AGE, exception3.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenCheckEntityAge() {
        ClientEntity clientEntity1 =
                new ClientEntity("Vag", "Prl", "asdf", LocalDate.of(1000, 1, 1), true);
        InstructorEntity instructorEntity =
                new InstructorEntity("Vag", "Prl", "asdf", LocalDate.of(2100, 1, 1), true);
        ClientEntity clientEntity3 =
                new ClientEntity("Vag", "Prl", "asdf", LocalDate.of(2021, 1, 1), true);

        CustomException exception1 =
                assertThrows(CustomException.class, () -> validationService.checkEntityAge(clientEntity1));
        CustomException exception2 =
                assertThrows(CustomException.class, () -> validationService.checkEntityAge(instructorEntity));
        CustomException exception3 =
                assertThrows(CustomException.class, () -> validationService.checkEntityAge(clientEntity3));

        assertEquals(WRONG_AGE, exception1.getMessage());
        assertEquals(WRONG_AGE, exception2.getMessage());
        assertEquals(WRONG_AGE, exception3.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenCheckEntityStatus() {
        ClientEntity clientEntity =
                new ClientEntity("Vag", "Prl", "asdf", LocalDate.of(2000, 1, 1), false);

        CustomException exception =
                assertThrows(CustomException.class, () -> validationService.checkEntityStatus(clientEntity));

        assertEquals("Person is not active", exception.getMessage());
    }
}