package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private String firstName;
    private String lastName;
    private String passport;
    private LocalDate birthdate;
    private boolean isActive;
    private Collection<WorkoutDto> clientWorkouts;
}