package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutClassDto;
import com.example.project.dto.WorkoutSessionDto;
import com.example.project.entity.WorkoutSessionEntity;
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

    void addInstructorToWorkoutByWorkoutNameAndInstructorId(String workoutName, Long instructorId);

    ClientDto deleteClientFromWorkoutByWorkoutIdAndClientId(Long workoutId, Long clientId);

    InstructorDto deleteInstructorFromWorkoutByWorkoutId(Long workoutId);

    WorkoutClassDto updateById(Long id, WorkoutClassDto workoutClassDto) throws JsonMappingException;

    List<ClientDto> getActiveClientsByClassName(String name);

    List<WorkoutSessionDto> getAllClassSessions(Long id);

    WorkoutSessionDto deleteSession(Long classId, Long sessionId);

    WorkoutSessionDto addSession(Long classId, WorkoutSessionDto workoutSessionDto);
}
