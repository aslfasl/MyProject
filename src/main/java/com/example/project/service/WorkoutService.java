package com.example.project.service;

import com.example.project.dto.WorkoutDto;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.time.Duration;
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
    // TODO: 13.03.2022  void deleteClientFromWorkoutByWorkoutIdAndClientPassport();
    // TODO: 13.03.2022   void deleteInstructorFromWorkoutByWorkoutIdAndInstructorId();
    WorkoutDto updateById(Long id, WorkoutDto workoutDto) throws JsonMappingException;
}
