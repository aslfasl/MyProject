package com.example.project.service;

import com.example.project.dto.WorkoutDto;

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


//    update
    // TODO: 07.03.2022

}
