package com.example.project.service;

import com.example.project.dto.Converter;
import com.example.project.dto.InstructorDto;
import com.example.project.entity.InstructorEntity;
import com.example.project.repo.InstructorRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Transactional
public class InstructorServiceImp implements InstructorService{

    private final InstructorRepo instructorRepo;
    private final Converter converter;

    @Override
    public InstructorDto getById(Long id) {
        return converter.convertInstructorEntity(instructorRepo.getById(id));
    }

    @Override
    public InstructorDto getByFullNameAndBirthdate(String firstName, String lastName, LocalDate birthdate) {
        return converter.convertInstructorEntity(
                instructorRepo.getInstructorEntityByFirstNameAndLastNameAndBirthdate(firstName, lastName, birthdate));
    }

    @Override
    public void deleteById(Long id) {
        instructorRepo.deleteById(id);
    }

    @Override
    public void update() {
// TODO: 07.03.2022
    }

    @Override
    public InstructorDto save(InstructorEntity instructor) {
        return converter.convertInstructorEntity(instructorRepo.save(instructor));
    }
}
