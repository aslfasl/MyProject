package com.example.project.service;

import com.example.project.entity.ClientEntity;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ValidationService {

    int minAge = 14;
    int maxAge = 90;

    boolean checkClientStatus(ClientEntity clientEntity){
        return clientEntity.isActive();
    }

    boolean checkInstructorStatus(InstructorEntity instructorEntity){
        return instructorEntity.isActive();
    }

    boolean checkIsWorkoutAvailable(WorkoutEntity workoutEntity){
        return workoutEntity.isAvailable();
    }

    // TODO: 15.03.2022
    boolean checkClientAge(ClientEntity clientEntity){
//        LocalDate today = LocalDate.now();
//        if (clientEntity.getBirthdate().isAfter(today)) {
//            throw new CustomException("This age is not allowed", ErrorType.WRONG_AGE);
//        } else {
//            int age = (int) ChronoUnit.YEARS.between(birthDate, today);
//            if (age < minAge || age > maxAge) {
//                throw new CustomException("This age is not allowed", ErrorType.WRONG_AGE);
//            }
//        }
        return true;
    }

    boolean checkInstructoAge(InstructorEntity instructorEntity){
        return false;
    }
}
