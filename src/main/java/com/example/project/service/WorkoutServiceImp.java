package com.example.project.service;

import com.example.project.dto.Converter;
import com.example.project.entity.WorkoutEntity;
import com.example.project.repo.WorkoutRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class WorkoutServiceImp implements WorkoutService{

    private final WorkoutRepo workoutRepo;
    private final Converter converter;

    // TODO: 08.03.2022 Begin with that class, need testing

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
