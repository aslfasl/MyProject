package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    String firstName;
    String lastName;
    boolean isActive;
    List<WorkoutDto> workouts;
}
