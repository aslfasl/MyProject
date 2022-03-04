package com.example.project.dto;

import com.example.project.db.entity.InstructorInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDto {

    String firstName;
    String lastName;
    WorkoutDto workout;
    InstructorInfoEntity instructorInfo;

}
