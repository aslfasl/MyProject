package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDto {

    private String name;
    private int durationInMinutes;
    private boolean isAvailable;
    private int peopleLimit;
    private int currentMembers;
    private Set<ClientDto> clients;
    private Set<InstructorDto> instructors;
}
