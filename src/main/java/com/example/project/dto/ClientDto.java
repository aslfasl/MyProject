package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    String firstName;
    String lastName;
    LocalDate birthdate;
    boolean isActive;
    List<WorkoutDto> workouts;
}
