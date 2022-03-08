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

    String firstName;
    String lastName;
    String passport;
    LocalDate birthdate;
    Set<WorkoutDto> instructorWorkouts;


}
