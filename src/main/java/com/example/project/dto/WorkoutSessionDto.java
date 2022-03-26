package com.example.project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSessionDto {

    private Long id;
    private Duration durationInMinutes;
    private LocalDate startDate;
    private LocalTime startTime;
    private WorkoutClassDto workoutClass;
}
