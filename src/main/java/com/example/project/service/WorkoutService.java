package com.example.project.service;

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
    void deleteClientFromWorkoutByWorkoutIdAndClientId(Long workoutId, Long clientId);
    void deleteInstructorFromWorkoutByWorkoutIdAndInstructorId(Long workoutId, Long instructorId);
    WorkoutDto updateById(Long id, WorkoutDto workoutDto) throws JsonMappingException;
}
