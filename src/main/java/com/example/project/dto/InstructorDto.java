package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String passport;
    private String address;
    private String phone;
    private String speciality;
    private String education;
    private boolean isActive;
    private LocalDate birthdate;
    private Set<WorkoutClassDto> instructorWorkouts;


}
