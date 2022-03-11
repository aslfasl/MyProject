package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.WorkoutEntity;

import java.util.List;

public interface WorkoutService {
    WorkoutDto getById(Long id);
    WorkoutEntity save(WorkoutDto workout);
    WorkoutDto getByName(String name);
    void deleteById(Long id);
    List<WorkoutDto> getAllAvailable();
    List<WorkoutDto> getAll();
    void addClientToWorkoutById(ClientDto clientDto, Long workoutId);
    void addInstructorToWorkoutById(InstructorDto instructorDto, Long workoutId);


//    update
    // TODO: 07.03.2022

}
