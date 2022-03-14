package com.example.project.service;

import com.example.project.dto.Converter;
import com.example.project.dto.InstructorDto;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.example.project.repo.InstructorRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class InstructorServiceImp implements InstructorService {

    private final InstructorRepo instructorRepo;
    private final Converter converter;

    @Override
    public InstructorDto getById(Long id) {
        return converter.convertInstructorEntity(instructorRepo.getById(id));
    }

    @Override
    public List<InstructorDto> getByFullName(String firstName, String lastName) {
        return instructorRepo.getInstructorEntityByFirstNameAndLastName(firstName, lastName).stream()
                .map(converter::convertInstructorEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        InstructorEntity instructor = instructorRepo.getById(id);
        instructor.setActive(false);
        instructorRepo.save(instructor);
    }

    @Override
    public void update() {
// TODO: 07.03.2022
    }

    @Override
    public InstructorEntity save(InstructorDto instructor) {
        if (instructorRepo.existsByPassport(instructor.getPassport())){
            throw new CustomException("Instructor with passport" + instructor.getPassport() + " already exists",
                    ErrorType.ALREADY_EXISTS);
        }
        return instructorRepo.save(converter.convertInstructorDto(instructor));
    }

    @Override
    public InstructorDto getByPassport(String passport) {
        InstructorEntity instructor = instructorRepo.findByPassport(passport);
        if (instructor==null){
            throw new CustomException("Instructor with passport " + passport + " not found",
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
