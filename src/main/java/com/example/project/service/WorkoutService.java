package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutDto;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.List;

public interface WorkoutService {
    WorkoutDto getById(Long id);

    WorkoutDto save(WorkoutDto workout);

    WorkoutDto getByName(String name);

    WorkoutDto deleteById(Long id);

    List<WorkoutDto> getAllAvailable();

    List<WorkoutDto> getAll();

    void addClientToWorkoutByWorkoutNameAndClientId(String workoutName, Long clientId);

    void addInstructorToWorkoutByWorkoutNameAndInstructorId(String workoutName, Long instructorId);

    ClientDto deleteClientFromWorkoutByWorkoutIdAndClientId(Long workoutId, Long clientId);

    InstructorDto deleteInstructorFromWorkoutByWorkoutIdAndInstructorId(Long workoutId, Long instructorId);

    WorkoutDto updateById(Long id, WorkoutDto workoutDto) throws JsonMappingException;

    List<ClientDto> getActiveClientsByWorkoutName(String name);

    List<InstructorDto> getActiveInstructorsByWorkoutName(String name);
}
