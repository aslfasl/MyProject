package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutClassDto;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.List;

public interface WorkoutClassService {
    WorkoutClassDto getById(Long id);

    WorkoutClassDto save(WorkoutClassDto workout);

    WorkoutClassDto getByName(String name);

    WorkoutClassDto deleteById(Long id);

    List<WorkoutClassDto> getAllAvailable();

    List<WorkoutClassDto> getAll();

    void addClientToWorkoutByWorkoutNameAndClientId(String workoutName, Long clientId);

//    void addInstructorToWorkoutByWorkoutNameAndInstructorId(String workoutName, Long instructorId);

    ClientDto deleteClientFromWorkoutByWorkoutIdAndClientId(Long workoutId, Long clientId);

//    InstructorDto deleteInstructorFromWorkoutByWorkoutIdAndInstructorId(Long workoutId, Long instructorId);

    WorkoutClassDto updateById(Long id, WorkoutClassDto workoutClassDto) throws JsonMappingException;

    List<ClientDto> getActiveClientsByWorkoutName(String name);

//    List<InstructorDto> getActiveInstructorsByWorkoutName(String name);
}
