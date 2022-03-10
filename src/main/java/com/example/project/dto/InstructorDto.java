package com.example.project.dto;

import com.example.project.entity.WorkoutEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDto {

    private String firstName;
    private String lastName;
    private String passport;
    private boolean isActive;
    private LocalDate birthdate;
    private Set<WorkoutDto> instructorWorkouts;


}
