package com.example.project.service;

import com.example.project.converter.Converter;
import com.example.project.dto.InstructorDto;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.example.project.repo.InstructorRepo;
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
public class InstructorServiceImp implements InstructorService {

    private final InstructorRepo instructorRepo;
    private final Converter converter;
    private final ObjectMapper objectMapper;
    private final ValidationService validationService;

    public void addWorkoutToInstructor(WorkoutEntity workout, InstructorEntity instructorEntity) {
        validationService.checkIsWorkoutAvailable(workout);
        if (instructorEntity.getInstructorWorkouts().contains(workout)) {
            throw new CustomException(INSTRUCTOR_ALREADY_SIGNED_FOR + workout.getName(), ErrorType.ALREADY_EXISTS);
        }
        instructorEntity.getInstructorWorkouts().add(workout);
        workout.getInstructors().add(instructorEntity);
    }

    @Override
    public InstructorDto getById(Long id) {
        Optional<InstructorEntity> instructorOpt = instructorRepo.findById(id);
        if (instructorOpt.isEmpty()) {
            throw new CustomException(INSTRUCTOR_NOT_FOUND_ID + id,
                    ErrorType.NOT_FOUND);
        }
        return converter.convertInstructorEntity(instructorOpt.get());
    }

    @Override
    public List<InstructorDto> getByFullName(String firstName, String lastName) {
        return instructorRepo.getInstructorEntityByFirstNameAndLastName(firstName, lastName).stream()
                .map(converter::convertInstructorEntity)
                .collect(Collectors.toList());
    }

    @Override
    public InstructorDto deleteById(Long id) {
        InstructorEntity instructor = instructorRepo.getById(id);
        instructor.setActive(false);
        return converter.convertInstructorEntity(instructorRepo.save(instructor));
    }

    @Override
    public InstructorDto updateById(Long id, InstructorDto instructorOverride) throws JsonMappingException {
        Optional<InstructorEntity> optionalInstructor = instructorRepo.findById(id);
        if (optionalInstructor.isEmpty()) {
            throw new CustomException(CLIENT_NOT_FOUND_ID + id,
                    ErrorType.NOT_FOUND);
        }
        InstructorEntity instructorToUpdate = optionalInstructor.get();
        if (instructorRepo.existsByPassport(instructorOverride.getPassport())) {
            throw new CustomException(CLIENT_ALREADY_EXISTS_PASSPORT + instructorOverride.getPassport(),
                    ErrorType.ALREADY_EXISTS);
        } else {
            instructorToUpdate = objectMapper.updateValue(instructorToUpdate, instructorOverride);
        }
        return converter.convertInstructorEntity(instructorRepo.save(instructorToUpdate));
    }

    @Override
    public InstructorDto save(InstructorDto instructor) {
        validationService.checkInstructorAge(instructor);
        validationService.checkInstructorStatus(instructor);
        if (instructorRepo.existsByPassport(instructor.getPassport())) {
            throw new CustomException(INSTRUCTOR_ALREADY_EXISTS_PASSPORT + instructor.getPassport(),
                    ErrorType.ALREADY_EXISTS);
        }
        InstructorEntity instructorEntity = instructorRepo.save(converter.convertInstructorDto(instructor));
        return converter.convertInstructorEntity(instructorEntity);
    }

    @Override
    public InstructorDto getByPassport(String passport) {
        InstructorEntity instructor = instructorRepo.findByPassport(passport);
        if (instructor == null) {
            throw new CustomException(INSTRUCTOR_NOT_FOUND_PASSPORT + passport,
                    ErrorType.NOT_FOUND);
        }
        return converter.convertInstructorEntity(instructor);
    }

    @Override
    public List<InstructorDto> getAllActive() {
        return instructorRepo.findAllByIsActiveTrue().stream()
                .map(converter::convertInstructorEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<InstructorDto> getAll() {
        return instructorRepo.findAll().stream()
                .map(converter::convertInstructorEntity)
                .collect(Collectors.toList());
    }
}
