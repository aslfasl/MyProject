package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutClassDto {

    private Long id;
    private String name;
    private Duration durationInMinutes;
    private boolean isAvailable;
    private Integer peopleLimit;
    private Set<ClientDto> clients;
    private InstructorDto instructor;
}
