package com.example.project.service;

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

//    update
    // TODO: 07.03.2022

}
