package com.example.project.service;

import com.example.project.converter.Converter;
import com.example.project.dto.ClientDto;
import com.example.project.dto.WorkoutClassDto;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.WorkoutClassEntity;
import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.example.project.repo.ClientRepo;
import com.example.project.repo.InstructorRepo;
import com.example.project.repo.WorkoutClassRepo;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.project.exception.ExceptionMessageUtils.*;

//@Service
//@AllArgsConstructor
//@Transactional
//public class WorkoutClassServiceImp implements WorkoutClassService {
//
//    private final WorkoutClassRepo workoutClassRepo;
//    private final ClientRepo clientRepo;
//    private final InstructorRepo instructorRepo;
//    private final Converter converter;
//    private final ObjectMapper objectMapper;
//    private final ValidationService validationService;

//    public void addInstructorToWorkout(InstructorEntity instructor, WorkoutClassEntity workoutClassEntity) {
//        validationService.checkEntityAge(instructor);
//        validationService.checkEntityStatus(instructor);
//        if (workoutClassEntity.getInstructors().contains(instructor)) {
//            throw new CustomException("Instructor " + instructor.getFirstName() + " already signed for this workout",
//                    ErrorType.ALREADY_EXISTS);
//        }
//        workoutClassEntity.getInstructors().add(instructor);
//        instructor.getInstructorWorkouts().add(workoutClassEntity);
//    }

//    public void addClientToWorkout(ClientEntity client, WorkoutClassEntity workoutClassEntity) {
//        validationService.checkEntityStatus(client);
//        validationService.checkEntityAge(client);
//        if (workoutClassEntity.getClients().contains(client)) {
//            throw new CustomException("Client " + client.getFirstName() + " already signed for this workout",
//                    ErrorType.ALREADY_EXISTS);
//        }
//        if (showActiveClientsCounter(workoutClassEntity) >= workoutClassEntity.getPeopleLimit()) {
//            throw new CustomException("All free slots has been taken for this workout", ErrorType.ALREADY_EXISTS);
//        }
//        workoutClassEntity.getClients().add(client);
//        client.getClientWorkouts().add(workoutClassEntity);
//    }
//
//    public long showActiveClientsCounter(WorkoutClassEntity workoutClassEntity) {
//        return workoutClassEntity.getClients().stream().filter(ClientEntity::isActive).count();
//    }
//
//    public List<ClientDto> getActiveClientsByWorkoutName(String name) {
//        WorkoutClassDto workoutClassDto = getByName(name);
//        if (workoutClassDto == null) {
//            throw new CustomException("Workout with name " + name + " not found", ErrorType.NOT_FOUND);
//        }
//        return workoutClassDto.getClients().stream()
//                .filter(ClientDto::isActive)
//                .collect(Collectors.toList());
//    }

//    public List<InstructorDto> getActiveInstructorsByWorkoutName(String name) {
//        WorkoutClassDto workoutClassDto = getByName(name);
//        if (workoutClassDto == null) {
//            throw new CustomException("Workout with name " + name + " not found", ErrorType.NOT_FOUND);
//        }
//        return workoutClassDto.getInstructors().stream()
//                .filter(InstructorDto::isActive)
//                .collect(Collectors.toList());
//    }

//    @Override
//    public WorkoutClassDto getById(Long id) {
//        return converter.convertWorkoutEntity(workoutClassRepo.findById(id)
//                .orElseThrow(() -> new CustomException(CLASS_NOT_FOUND_ID + id, ErrorType.NOT_FOUND)));
//
//    }
//
//    @Override
//    public WorkoutClassDto save(WorkoutClassDto workoutClassDto) {
//        if (workoutClassRepo.existsByNameAndDurationInMinutesAndPeopleLimit(workoutClassDto.getName(),
//                workoutClassDto.getDurationInMinutes(),
//                workoutClassDto.getPeopleLimit())) {
//            throw new CustomException(CLASS_ALREADY_EXISTS_NAME + workoutClassDto.getName(),
//                    ErrorType.ALREADY_EXISTS);
//        }
//        WorkoutClassEntity workoutClassEntity = converter.convertWorkoutDto(workoutClassDto);
//        return converter.convertWorkoutEntity(workoutClassRepo.save(workoutClassEntity));
//    }
//
//    @Override
//    public WorkoutClassDto getByName(String name) {
//        WorkoutClassEntity workoutClassEntity = workoutClassRepo.findByName(name);
//        if (workoutClassEntity != null) {
//            return converter.convertWorkoutEntity(workoutClassEntity);
//        } else {
//            throw new CustomException(CLASS_NOT_FOUND_NAME + name, ErrorType.NOT_FOUND);
//        }
//    }
//
//    @Override
//    public WorkoutClassDto deleteById(Long id) {
//        WorkoutClassEntity workout = workoutClassRepo.findById(id)
//                .orElseThrow(() -> new CustomException(CLASS_NOT_FOUND_ID + id, ErrorType.NOT_FOUND));
//        workout.setAvailable(false);
//        return converter.convertWorkoutEntity(workoutClassRepo.save(workout));
//    }
//
//    @Override
//    public List<WorkoutClassDto> getAllAvailable() {
//        return workoutClassRepo.findAllByIsAvailableTrue().stream()
//                .map(converter::convertWorkoutEntity)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<WorkoutClassDto> getAll() {
//        return workoutClassRepo.findAll().stream()
//                .map(converter::convertWorkoutEntity)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void addClientToWorkoutByWorkoutNameAndClientId(String workoutName, Long clientId) {
//        WorkoutClassEntity workoutClassEntity = workoutClassRepo.findByName(workoutName);
//        ClientEntity clientEntity = clientRepo.findClientById(clientId);
//        if (clientEntity == null) {
//            throw new CustomException(CLIENT_NOT_FOUND_ID + clientId,
//                    ErrorType.NOT_FOUND);
//        }
//        validationService.checkEntityAge(clientEntity);
//        validationService.checkEntityStatus(clientEntity);
//        if (workoutClassEntity == null) {
//            throw new CustomException(CLASS_NOT_FOUND_NAME + workoutName, ErrorType.NOT_FOUND);
//        }
//        addClientToWorkout(clientEntity, workoutClassEntity);
//    }

//    @Override
//    public void addInstructorToWorkoutByWorkoutNameAndInstructorId(String workoutName, Long instructorId) {
//        WorkoutClassEntity workoutClassEntity = classRepo.findByName(workoutName);
//        Optional<InstructorEntity> instructorOptional = instructorRepo.findById(instructorId);
//        if (workoutClassEntity == null) {
//            throw new CustomException(CLASS_NOT_FOUND_NAME + workoutName, ErrorType.NOT_FOUND);
//        }
//        if (instructorOptional.isEmpty()) {
//            throw new CustomException(INSTRUCTOR_NOT_FOUND_ID + instructorId, ErrorType.NOT_FOUND);
//        }
//        addInstructorToWorkout(instructorOptional.get(), workoutClassEntity);
//    }

//    @Override
//    public WorkoutClassDto updateById(Long id, WorkoutClassDto workoutOverride) throws JsonMappingException {
//        Optional<WorkoutClassEntity> workoutOptional = workoutClassRepo.findById(id);
//        if (workoutOptional.isEmpty()) {
//            throw new CustomException(CLASS_NOT_FOUND_ID + id, ErrorType.NOT_FOUND);
//        }
//        if (workoutClassRepo.existsByName(workoutOverride.getName())) {
//            throw new CustomException(CLASS_ALREADY_EXISTS_NAME + workoutOverride.getName(),
//                    ErrorType.ALREADY_EXISTS);
//        }
//        WorkoutClassEntity workoutToUpdate = workoutOptional.get();
//        workoutToUpdate = objectMapper.updateValue(workoutToUpdate, workoutOverride);
//        return converter.convertWorkoutEntity(workoutClassRepo.save(workoutToUpdate));
//    }
//
//    @Override
//    public ClientDto deleteClientFromWorkoutByWorkoutIdAndClientId(Long workoutId, Long clientId) {
//        Optional<WorkoutClassEntity> workoutOpt = workoutClassRepo.findById(workoutId);
//        Optional<ClientEntity> clientOpt = clientRepo.findById(clientId);
//        if (workoutOpt.isEmpty()) {
//            throw new CustomException(CLASS_NOT_FOUND_ID + workoutId, ErrorType.NOT_FOUND);
//        }
//        if (clientOpt.isEmpty()) {
//            throw new CustomException(CLIENT_NOT_FOUND_ID + clientId, ErrorType.NOT_FOUND);
//        }
//        WorkoutClassEntity workoutClassEntity = workoutOpt.get();
//        ClientEntity clientEntity = clientOpt.get();
//        workoutClassEntity.getClients().remove(clientEntity);
//        clientEntity.getClientWorkouts().remove(workoutClassEntity);
//        return converter.convertClientEntity(clientEntity);
//    }

//    @Override
//    public InstructorDto deleteInstructorFromWorkoutByWorkoutIdAndInstructorId(Long workoutId, Long instructorId) {
//        Optional<WorkoutClassEntity> workoutOpt = classRepo.findById(workoutId);
//        Optional<InstructorEntity> instructorOpt = instructorRepo.findById(instructorId);
//        if (workoutOpt.isEmpty()) {
//            throw new CustomException(CLASS_NOT_FOUND_ID + workoutId, ErrorType.NOT_FOUND);
//        }
//        if (instructorOpt.isEmpty()) {
//            throw new CustomException(INSTRUCTOR_NOT_FOUND_ID + instructorId, ErrorType.NOT_FOUND);
//        }
//        WorkoutClassEntity workoutClassEntity = workoutOpt.get();
//        InstructorEntity instructorEntity = instructorOpt.get();
//        workoutClassEntity.getInstructors().remove(instructorEntity);
//        instructorEntity.getInstructorWorkouts().remove(workoutClassEntity);
//        return converter.convertInstructorEntity(instructorEntity);
//    }
//}
