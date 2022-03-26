package com.example.project.service;

import com.example.project.converter.Converter;
import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutClassDto;
import com.example.project.dto.WorkoutSessionDto;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutClassEntity;
import com.example.project.entity.WorkoutSessionEntity;
import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.example.project.repo.ClientRepo;
import com.example.project.repo.InstructorRepo;
import com.example.project.repo.WorkoutClassRepo;
import com.example.project.repo.WorkoutSessionRepo;
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
public class WorkoutClassServiceImp implements WorkoutClassService {

    private final WorkoutClassRepo workoutClassRepo;
    private final ClientRepo clientRepo;
    private final WorkoutSessionRepo workoutSessionRepo;
    private final InstructorRepo instructorRepo;
    private final Converter converter;
    private final ObjectMapper objectMapper;
    private final ValidationService validationService;

    public void addInstructorToClass(InstructorEntity instructor, WorkoutClassEntity workoutClassEntity) {
        validationService.checkEntityAge(instructor);
        validationService.checkInstructorStatus(instructor);
        if (workoutClassEntity.getInstructor() != null && workoutClassEntity.getInstructor().equals(instructor)) {
            throw new CustomException("Instructor " + instructor.getFirstName() + " already signed for this workout",
                    ErrorType.ALREADY_EXISTS);
        }
        workoutClassEntity.setInstructor(instructor);
        instructor.getInstructorWorkouts().add(workoutClassEntity);
    }

    public void addClientToClass(ClientEntity client, WorkoutClassEntity workoutClassEntity) {
        validationService.checkClientEntityStatus(client);
        validationService.checkEntityAge(client);
        if (workoutClassEntity.getClients().contains(client)) {
            throw new CustomException("Client " + client.getFirstName() + " already signed for this workout",
                    ErrorType.ALREADY_EXISTS);
        }
        if (showActiveClientsCounter(workoutClassEntity) >= workoutClassEntity.getPeopleLimit()) {
            throw new CustomException("All free slots has been taken for this workout", ErrorType.ALREADY_EXISTS);
        }
        workoutClassEntity.getClients().add(client);
        client.getClientWorkouts().add(workoutClassEntity);
    }

    public void addSessionToClass(WorkoutSessionEntity session, WorkoutClassEntity workoutClass) {
        if (workoutClass.getSessions().contains(session)) {
            throw new CustomException("Workout class " + workoutClass.getName() + " already has session: "
                    + session.getStartDate() + " " + session.getStartTime(), ErrorType.ALREADY_EXISTS);
        }
        session.setWorkoutClass(workoutClass);
        workoutClass.getSessions().add(session);
    }

    public long showActiveClientsCounter(WorkoutClassEntity workoutClassEntity) {
        return workoutClassEntity.getClients().stream()
                .filter(clientEntity -> clientEntity.getMembership().isActive())
                .count();
    }

    public List<ClientDto> getActiveClientsByClassName(String name) {
        WorkoutClassEntity workoutClassEntity = workoutClassRepo.findByName(name);
        if (workoutClassEntity == null) {
            throw new CustomException("Workout with name " + name + " not found", ErrorType.NOT_FOUND);
        }
        return workoutClassEntity.getClients().stream()
                .filter(clientDto -> clientDto.getMembership().isActive())
                .map(converter::convertClientEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkoutSessionDto> getAllClassSessions(Long id) {
        WorkoutClassEntity workoutClassEntity = workoutClassRepo.findById(id)
                .orElseThrow(() -> new CustomException(CLASS_NOT_FOUND_ID + id, ErrorType.NOT_FOUND));
        return workoutClassEntity.getSessions().stream()
                .map(converter::convertSessionEntity)
                .collect(Collectors.toList());
    }

    @Override
    public WorkoutSessionDto deleteSession(Long classId, Long sessionId) {
        WorkoutClassEntity workoutClassEntity = workoutClassRepo.findById(classId)
                .orElseThrow(() -> new CustomException(CLASS_NOT_FOUND_ID + classId, ErrorType.NOT_FOUND));
        WorkoutSessionEntity workoutSession = workoutSessionRepo.findById(sessionId).
                orElseThrow(() -> new CustomException("Session with id " + sessionId + " not found", ErrorType.NOT_FOUND));
        workoutClassEntity.getSessions().remove(workoutSession);
        return converter.convertSessionEntity(workoutSession);
    }

    @Override
    public WorkoutSessionDto addSession(Long classId, WorkoutSessionDto workoutSessionDto) {
        WorkoutClassEntity workoutClassEntity = workoutClassRepo.findById(classId)
                .orElseThrow(() -> new CustomException(CLASS_NOT_FOUND_ID + classId, ErrorType.NOT_FOUND));
        WorkoutSessionEntity workoutSession = converter.convertSessionDto(workoutSessionDto);
        addSessionToClass(workoutSession, workoutClassEntity);
        return converter.convertSessionEntity(workoutSession);
    }

    @Override
    public WorkoutClassDto getById(Long id) {
        return converter.convertWorkoutClassEntity(workoutClassRepo.findById(id)
                .orElseThrow(() -> new CustomException(CLASS_NOT_FOUND_ID + id, ErrorType.NOT_FOUND)));

    }

    @Override
    public WorkoutClassDto save(WorkoutClassDto workoutClassDto) {
        if (workoutClassRepo.existsByName(workoutClassDto.getName())) {
            throw new CustomException(CLASS_ALREADY_EXISTS_NAME + workoutClassDto.getName(),
                    ErrorType.ALREADY_EXISTS);
        }
        WorkoutClassEntity workoutClassEntity = converter.convertWorkoutClassDto(workoutClassDto);
        return converter.convertWorkoutClassEntity(workoutClassRepo.save(workoutClassEntity));
    }

    @Override
    public WorkoutClassDto getByName(String name) {
        WorkoutClassEntity workoutClassEntity = workoutClassRepo.findByName(name);
        if (workoutClassEntity != null) {
            return converter.convertWorkoutClassEntity(workoutClassEntity);
        } else {
            throw new CustomException(CLASS_NOT_FOUND_NAME + name, ErrorType.NOT_FOUND);
        }
    }

    @Override
    public WorkoutClassDto deleteById(Long id) {
        WorkoutClassEntity workout = workoutClassRepo.findById(id)
                .orElseThrow(() -> new CustomException(CLASS_NOT_FOUND_ID + id, ErrorType.NOT_FOUND));
        workout.setAvailable(false);
        return converter.convertWorkoutClassEntity(workoutClassRepo.save(workout));
    }

    @Override
    public List<WorkoutClassDto> getAllAvailable() {
        return workoutClassRepo.findAllByIsAvailableTrue().stream()
                .map(converter::convertWorkoutClassEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkoutClassDto> getAll() {
        return workoutClassRepo.findAll().stream()
                .map(converter::convertWorkoutClassEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void addClientToWorkoutByWorkoutNameAndClientId(String workoutName, Long clientId) {
        WorkoutClassEntity workoutClassEntity = workoutClassRepo.findByName(workoutName);
        ClientEntity clientEntity = clientRepo.findById(clientId)
                .orElseThrow(() -> new CustomException(CLIENT_NOT_FOUND_ID + clientId, ErrorType.NOT_FOUND));
        validationService.checkEntityAge(clientEntity);
        validationService.checkClientEntityStatus(clientEntity);
        if (workoutClassEntity == null) {
            throw new CustomException(CLASS_NOT_FOUND_NAME + workoutName, ErrorType.NOT_FOUND);
        }
        addClientToClass(clientEntity, workoutClassEntity);
    }

    @Override
    public void addInstructorToWorkoutByWorkoutNameAndInstructorId(String workoutName, Long instructorId) {
        WorkoutClassEntity workoutClassEntity = workoutClassRepo.findByName(workoutName);
        if (workoutClassEntity == null) {
            throw new CustomException(CLASS_NOT_FOUND_NAME + workoutName, ErrorType.NOT_FOUND);
        }
        InstructorEntity instructorEntity = instructorRepo.findById(instructorId).
                orElseThrow(() -> new CustomException(INSTRUCTOR_NOT_FOUND_ID + instructorId, ErrorType.NOT_FOUND));
        addInstructorToClass(instructorEntity, workoutClassEntity);
    }

    @Override
    public WorkoutClassDto updateById(Long id, WorkoutClassDto workoutOverride) throws JsonMappingException {
        Optional<WorkoutClassEntity> workoutOptional = workoutClassRepo.findById(id);
        if (workoutOptional.isEmpty()) {
            throw new CustomException(CLASS_NOT_FOUND_ID + id, ErrorType.NOT_FOUND);
        }
        if (workoutClassRepo.existsByName(workoutOverride.getName())) {
            throw new CustomException(CLASS_ALREADY_EXISTS_NAME + workoutOverride.getName(),
                    ErrorType.ALREADY_EXISTS);
        }
        WorkoutClassEntity workoutToUpdate = workoutOptional.get();
        workoutToUpdate = objectMapper.updateValue(workoutToUpdate, workoutOverride);
        return converter.convertWorkoutClassEntity(workoutClassRepo.save(workoutToUpdate));
    }

    @Override
    public ClientDto deleteClientFromWorkoutByWorkoutIdAndClientId(Long workoutId, Long clientId) {
        Optional<WorkoutClassEntity> workoutOpt = workoutClassRepo.findById(workoutId);
        Optional<ClientEntity> clientOpt = clientRepo.findById(clientId);
        if (workoutOpt.isEmpty()) {
            throw new CustomException(CLASS_NOT_FOUND_ID + workoutId, ErrorType.NOT_FOUND);
        }
        if (clientOpt.isEmpty()) {
            throw new CustomException(CLIENT_NOT_FOUND_ID + clientId, ErrorType.NOT_FOUND);
        }
        WorkoutClassEntity workoutClassEntity = workoutOpt.get();
        ClientEntity clientEntity = clientOpt.get();
        workoutClassEntity.getClients().remove(clientEntity);
        clientEntity.getClientWorkouts().remove(workoutClassEntity);
        return converter.convertClientEntity(clientEntity);
    }

    @Override
    public InstructorDto deleteInstructorFromWorkoutByWorkoutId(Long workoutId) {
        WorkoutClassEntity workoutClassEntity = workoutClassRepo.findById(workoutId)
                .orElseThrow(() -> new CustomException(CLASS_NOT_FOUND_ID + workoutId, ErrorType.NOT_FOUND));
        InstructorEntity instructor = workoutClassEntity.getInstructor();
        instructor.getInstructorWorkouts().remove(workoutClassEntity);
        workoutClassEntity.setInstructor(null);
        return converter.convertInstructorEntity(instructor);
    }
}
