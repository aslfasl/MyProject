package com.example.project.service;

import com.example.project.converter.Converter;
import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.example.project.repo.ClientRepo;
import com.example.project.repo.InstructorRepo;
import com.example.project.repo.WorkoutRepo;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.project.exception.ExceptionMessageUtils.*;

@Service
@AllArgsConstructor
@Transactional
public class WorkoutServiceImp implements WorkoutService {

    private final WorkoutRepo workoutRepo;
    private final ClientRepo clientRepo;
    private final InstructorRepo instructorRepo;
    private final Converter converter;
    private final ObjectMapper objectMapper;
    private final ValidationService validationService;

    public void addInstructorToWorkout(InstructorEntity instructor, WorkoutEntity workoutEntity) {
        validationService.checkEntityAge(instructor);
        validationService.checkEntityStatus(instructor);
        if (workoutEntity.getInstructors().contains(instructor)) {
            throw new CustomException("Instructor " + instructor.getFirstName() + " already signed for this workout",
                    ErrorType.ALREADY_EXISTS);
        }
        workoutEntity.getInstructors().add(instructor);
        instructor.getInstructorWorkouts().add(workoutEntity);
    }

    public void addClientToWorkout(ClientEntity client, WorkoutEntity workoutEntity) {
        validationService.checkEntityStatus(client);
        validationService.checkEntityAge(client);
        if (workoutEntity.getClients().contains(client)) {
            throw new CustomException("Client " + client.getFirstName() + " already signed for this workout",
                    ErrorType.ALREADY_EXISTS);
        }
        if (showActiveClientsCounter(workoutEntity) >= workoutEntity.getPeopleLimit()) {
            throw new CustomException("All free slots has been taken for this workout", ErrorType.ALREADY_EXISTS);
        }
        workoutEntity.getClients().add(client);
        client.getClientWorkouts().add(workoutEntity);
    }

    public long showActiveClientsCounter(WorkoutEntity workoutEntity) {
        return workoutEntity.getClients().stream().filter(ClientEntity::isActive).count();
    }

    @Override
    public WorkoutDto getById(Long id) {
        Optional<WorkoutEntity> byId = workoutRepo.findById(id);
        if (byId.isPresent()) {
            return converter.convertWorkoutEntity(byId.get());
        } else {
            throw new CustomException(WORKOUT_NOT_FOUND_ID + id, ErrorType.NOT_FOUND);
        }
    }

    @Override
    public WorkoutDto save(WorkoutDto workoutDto) {
        if (workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(workoutDto.getName(),
                workoutDto.getDurationInMinutes(),
                workoutDto.getPeopleLimit())) {
            throw new CustomException(WORKOUT_ALREADY_EXISTS_NAME + workoutDto.getName(),
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
            throw new CustomException(WORKOUT_NOT_FOUND_NAME + name, ErrorType.NOT_FOUND);
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
        validationService.checkEntityAge(clientEntity);
        validationService.checkEntityStatus(clientEntity);
        if (workoutEntity == null) {
            throw new CustomException(WORKOUT_NOT_FOUND_NAME + workoutName, ErrorType.NOT_FOUND);
        }
        addClientToWorkout(clientEntity, workoutEntity);
    }

    @Override
    public void addInstructorToWorkoutByWorkoutNameAndInstructorId(String workoutName, Long instructorId) {
        WorkoutEntity workoutEntity = workoutRepo.findByName(workoutName);
        Optional<InstructorEntity> instructorOptional = instructorRepo.findById(instructorId);
        if (workoutEntity == null) {
            throw new CustomException(WORKOUT_NOT_FOUND_NAME + workoutName, ErrorType.NOT_FOUND);
        }
        if (instructorOptional.isEmpty()) {
            throw new CustomException(INSTRUCTOR_NOT_FOUND_ID + instructorId, ErrorType.NOT_FOUND);
        }
        addInstructorToWorkout(instructorOptional.get(), workoutEntity);
    }

    @Override
    public WorkoutDto updateById(Long id, WorkoutDto workoutOverride) throws JsonMappingException {
        Optional<WorkoutEntity> workoutOptional = workoutRepo.findById(id);
        if (workoutOptional.isEmpty()) {
            throw new CustomException(WORKOUT_NOT_FOUND_ID + id, ErrorType.NOT_FOUND);
        }
        if (workoutRepo.existsByName(workoutOverride.getName())) {
            throw new CustomException(WORKOUT_ALREADY_EXISTS_NAME + workoutOverride.getName(),
                    ErrorType.ALREADY_EXISTS);
        }
        WorkoutEntity workoutToUpdate = workoutOptional.get();
        workoutToUpdate = objectMapper.updateValue(workoutToUpdate, workoutOverride);
        return converter.convertWorkoutEntity(workoutRepo.save(workoutToUpdate));
    }

    @Override
    public ClientDto deleteClientFromWorkoutByWorkoutIdAndClientId(Long workoutId, Long clientId) {
        Optional<WorkoutEntity> workoutOpt = workoutRepo.findById(workoutId);
        Optional<ClientEntity> clientOpt = clientRepo.findById(clientId);
        if (workoutOpt.isEmpty()) {
            throw new CustomException(WORKOUT_NOT_FOUND_ID + workoutId, ErrorType.NOT_FOUND);
        }
        if (clientOpt.isEmpty()){
            throw new CustomException(CLIENT_NOT_FOUND_ID + clientId, ErrorType.NOT_FOUND);
        }
        WorkoutEntity workoutEntity = workoutOpt.get();
        ClientEntity clientEntity = clientOpt.get();
        workoutEntity.getClients().remove(clientEntity);
        clientEntity.getClientWorkouts().remove(workoutEntity);
        return converter.convertClientEntity(clientEntity);
    }

    @Override
    public InstructorDto deleteInstructorFromWorkoutByWorkoutIdAndInstructorId(Long workoutId, Long instructorId) {
        Optional<WorkoutEntity> workoutOpt = workoutRepo.findById(workoutId);
        Optional<InstructorEntity> instructorOpt = instructorRepo.findById(instructorId);
        if (workoutOpt.isEmpty()) {
            throw new CustomException(WORKOUT_NOT_FOUND_ID + workoutId, ErrorType.NOT_FOUND);
        }
        if (instructorOpt.isEmpty()){
            throw new CustomException(INSTRUCTOR_NOT_FOUND_ID + instructorId, ErrorType.NOT_FOUND);
        }
        WorkoutEntity workoutEntity = workoutOpt.get();
        InstructorEntity instructorEntity = instructorOpt.get();
        workoutEntity.getInstructors().remove(instructorEntity);
        instructorEntity.getInstructorWorkouts().remove(workoutEntity);
        return converter.convertInstructorEntity(instructorEntity);
    }
}
