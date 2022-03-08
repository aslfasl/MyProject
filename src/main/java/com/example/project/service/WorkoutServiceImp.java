package com.example.project.service;

import com.example.project.db.entity.WorkoutEntity;
import com.example.project.db.repo.WorkoutRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class WorkoutServiceImp implements WorkoutService{

    private final WorkoutRepo workoutRepo;

    @Override
    public WorkoutEntity getById(Long id) {
        return workoutRepo.findById(id).get(); // TODO: 07.03.2022
    }

    @Override
    public WorkoutEntity save(WorkoutEntity workout) {
        return null;
    }

    @Override
    public WorkoutEntity getByName(String name) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
