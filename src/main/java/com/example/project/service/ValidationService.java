package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.entity.BaseEntity;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutClassEntity;
import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.example.project.exception.ExceptionMessageUtils.WRONG_AGE;

@Service
@Transactional
public class ValidationService {

    int minAge = 7;
    int maxAge = 90;

    void checkClientStatus(ClientDto client) {
        if (!client.getMembership().isActive()) {
            throw new CustomException("Client is not active", ErrorType.INACTIVE);
        }
    }

    void checkInstructorStatus(InstructorDto instructor) {
        if (!instructor.isActive()) {
            throw new CustomException("Instructor is not active", ErrorType.INACTIVE);
        }
    }
    void checkInstructorStatus(InstructorEntity instructor) {
        if (!instructor.isActive()) {
            throw new CustomException("Instructor is not active", ErrorType.INACTIVE);
        }
    }

    void checkIsWorkoutAvailable(WorkoutClassEntity workoutClassEntity) {
        if (!workoutClassEntity.isAvailable()) {
            throw new CustomException("Workout is not available", ErrorType.INACTIVE);
        }
    }

    void checkClientAge(ClientDto client) {
        LocalDate today = LocalDate.now();
        int age = (int) ChronoUnit.YEARS.between(client.getBirthdate(), today);
        if (client.getBirthdate().isAfter(today) || age < minAge || age > maxAge) {
            throw new CustomException(WRONG_AGE, ErrorType.WRONG_AGE);
        }
    }

    void checkInstructorAge(InstructorDto instructor) {
        LocalDate today = LocalDate.now();
        int age = (int) ChronoUnit.YEARS.between(instructor.getBirthdate(), today);
        if (instructor.getBirthdate().isAfter(today) || age < minAge || age > maxAge) {
            throw new CustomException(WRONG_AGE, ErrorType.WRONG_AGE);
        }
    }

    void checkEntityAge(BaseEntity entity) {
        LocalDate today = LocalDate.now();
        int age = (int) ChronoUnit.YEARS.between(entity.getBirthdate(), today);
        if (entity.getBirthdate().isAfter(today) || age < minAge || age > maxAge) {
            throw new CustomException(WRONG_AGE, ErrorType.WRONG_AGE);
        }
    }

    void checkClientEntityStatus(ClientEntity client) {
        if (!client.getMembership().isActive()) {
            throw new CustomException("Person is not active", ErrorType.INACTIVE);
        }
    }

    void checkInstructorEntityStatus(InstructorEntity instructor) {
        if (!instructor.isActive()) {
            throw new CustomException("Person is not active", ErrorType.INACTIVE);
        }
    }
}
