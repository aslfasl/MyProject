package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.Converter;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.WorkoutEntity;
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
        if (workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(workoutDto.getName(),
                workoutDto.getDurationInMinutes(),
                workoutDto.getPeopleLimit())) {
            throw new RuntimeException("already exists"); // TODO: 10.03.2022
        }
        return workoutRepo.save(converter.convertWorkoutDto(workoutDto));
    }

    @Override
    public WorkoutDto getByName(String name) {
        WorkoutEntity workoutEntity = workoutRepo.findByName(name);
        if (workoutEntity != null) {
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

//    @Override
//    public void addClientToWorkoutByWorkoutNameAndClientPassport(String clientPassport, String workoutName) {
//        WorkoutEntity workoutEntity = workoutRepo.findByName(workoutName);
//        ClientEntity clientEntity = clientRepo.findClientEntityByPassport(clientPassport);
//        if (workoutEntity==null) {
//            throw new RuntimeException("no such workout..."); // TODO: 11.03.2022
//        }
//        workoutEntity.addClient(clientEntity);
//    }
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
