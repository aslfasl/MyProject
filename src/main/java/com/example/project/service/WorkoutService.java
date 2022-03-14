package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.WorkoutEntity;

import java.util.List;

public interface WorkoutService {
    WorkoutDto getById(Long id);
    WorkoutDto save(WorkoutDto workout);
    WorkoutDto getByName(String name);
    WorkoutDto deleteById(Long id);
    List<WorkoutDto> getAllAvailable();
    List<WorkoutDto> getAll();
    // TODO: 13.03.2022  void addClientToWorkoutByWorkoutNameAndClientPassport(String clientPassport, String workoutName);
    // TODO: 13.03.2022  void addInstructorToWorkoutByName(InstructorDto instructorDto, String workoutName);
    // TODO: 13.03.2022  void deleteClientFromWorkoutByWorkoutIdAndClientPassport();
    // TODO: 13.03.2022   void deleteInstructorFromWorkoutByWorkoutIdAndInstructorId();


//    update
    // TODO: 07.03.2022

}
