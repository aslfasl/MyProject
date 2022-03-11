package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.Converter;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.WorkoutEntity;
import com.example.project.repo.WorkoutRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class WorkoutServiceImp implements WorkoutService{

    private final WorkoutRepo workoutRepo;
    private final Converter converter;

    // TODO: 08.03.2022 Begin with that class, need testing

    @Override
    public WorkoutDto getById(Long id) {
        Optional<WorkoutEntity> byId = workoutRepo.findById(id);
        if (byId.isPresent()) {
            return converter.convertWorkoutEntity(byId.get());
        } else {
            throw new RuntimeException("no workout"); // TODO: 10.03.2022
        }
    }

    @Override
    public WorkoutEntity save(WorkoutDto workoutDto) {
        if (workoutRepo.existsByNameAndDurationInMinutes(workoutDto.getName(), workoutDto.getDurationInMinutes())){
            throw new RuntimeException("already exists"); // TODO: 10.03.2022
        }
        return workoutRepo.save(converter.convertWorkoutDto(workoutDto));
    }

    @Override
    public WorkoutDto getByName(String name) {
        WorkoutEntity workoutEntity = workoutRepo.findByName(name);
        if (workoutEntity!=null) {
            return converter.convertWorkoutEntity(workoutEntity);
        } else {
            throw new RuntimeException("no workout with that name"); // TODO: 10.03.2022
        }
    }

    @Override
    public void deleteById(Long id) {
        WorkoutEntity workout = workoutRepo.getById(id);
        workout.setAvailable(false);
        workoutRepo.save(workout);
    }

    @Override
    public List<WorkoutDto> getAllAvailable() {
        return workoutRepo.findAllByIsAvailableTrue().stream()
                .map(converter::convertWorkoutEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkoutDto> getAll() {
        return workoutRepo.findAll().stream()
                .map(converter::convertWorkoutEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void addClientToWorkoutById(ClientDto clientDto, Long workoutId) {
        Optional<WorkoutEntity> workoutById = workoutRepo.findById(workoutId);
        if (workoutById.isEmpty()) {
            throw new RuntimeException("no such workout..."); // TODO: 11.03.2022
        }
        workoutById.get().addClient(converter.convertClientDto(clientDto));
    }

    @Override
    public void addInstructorToWorkoutById(InstructorDto instructorDto, Long workoutId) {

    }
}
