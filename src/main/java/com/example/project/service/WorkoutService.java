package com.example.project.service;

import com.example.project.db.entity.WorkoutEntity;

public interface WorkoutService {
    WorkoutEntity getById(Long id);
    WorkoutEntity save(WorkoutEntity workout);
    WorkoutEntity getByName(String name);
    void deleteById(Long id);
//    update
    // TODO: 07.03.2022

}
