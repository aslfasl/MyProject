package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.Converter;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.example.project.repo.ClientRepo;
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
public class WorkoutServiceImp implements WorkoutService {

    private final WorkoutRepo workoutRepo;
    private final ClientRepo clientRepo;
    private final Converter converter;


    @Override
    public WorkoutDto getById(Long id) {
        Optional<WorkoutEntity> byId = workoutRepo.findById(id);
        if (byId.isPresent()) {
            return converter.convertWorkoutEntity(byId.get());
        } else {
            throw new CustomException("Workout with id " + id + " not found",
                    ErrorType.NOT_FOUND);
        }
    }

    @Override
    public WorkoutDto save(WorkoutDto workoutDto) {
        if (workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(workoutDto.getName(),
                workoutDto.getDurationInMinutes(),
                workoutDto.getPeopleLimit())) {
            throw new CustomException("Workout " + workoutDto.getName() + " already exists",
                    ErrorType.ALREADY_EXISTS);
        }
        WorkoutEntity workoutEntity = converter.convertWorkoutDto(workoutDto);
        return converter.convertWorkoutEntity(workoutRepo.save(workoutEntity));
    }

    @Override
    public WorkoutDto getByName(String name) {
        WorkoutEntity workoutEntity = workoutRepo.findByName(name);
        if (workoutEntity != null) {
            return converter.convertWorkoutEntity(workoutEntity);
        } else {
            throw new CustomException("Workout with name " + name + " not found",
                    ErrorType.NOT_FOUND);
        }
    }

    @Override
    public WorkoutDto deleteById(Long id) {
        WorkoutEntity workout = workoutRepo.getById(id);
        workout.setAvailable(false);
        WorkoutEntity workoutEntity = workoutRepo.save(workout);
        return converter.convertWorkoutEntity(workoutEntity);
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
    public void addClientToWorkoutByWorkoutNameAndClientId(String workoutName, Long clientId) {
        WorkoutEntity workoutEntity = workoutRepo.findByName(workoutName);
        ClientEntity clientEntity = clientRepo.findClientById(clientId);
        if (workoutEntity == null) {
            throw new CustomException("Workout with name " + workoutName + " not found",
                    ErrorType.NOT_FOUND);
        }
        if (clientEntity == null) {
            throw new CustomException("Client with id " + clientId + " not found",
                    ErrorType.NOT_FOUND);
        }
        workoutEntity.addClient(clientEntity);
    }
//
//    @Override
//    public void addInstructorToWorkoutByName(InstructorDto instructorDto, String workoutName) {
//        WorkoutEntity workoutEntity = workoutRepo.findByName(workoutName);
//        if (workoutEntity==null) {
//            throw new RuntimeException("no such workout..."); // TODO: 11.03.2022
//        }
//        workoutEntity.addInstructor(converter.convertInstructorDto(instructorDto));
//    }
//
//    @Override
//    public void deleteClientFromWorkoutByWorkoutIdAndClientPassport() {
//        WorkoutEntity workoutEntity = workoutRepo.findByName();
//        if (workoutEntity==null) {
//            throw new RuntimeException("no such workout..."); // TODO: 11.03.2022
//        }
//    }
//
//    @Override
//    public void deleteInstructorFromWorkoutByWorkoutIdAndInstructorId() {
//        // TODO: 13.03.2022
//    }
}
