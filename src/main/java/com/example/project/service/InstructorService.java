package com.example.project.service;

import com.example.project.dto.InstructorDto;
import com.example.project.entity.InstructorEntity;

import java.time.LocalDate;

public interface InstructorService {

    InstructorDto getById(Long id);
    InstructorDto getByFullNameAndBirthdate(String firstName, String lastName, LocalDate birthdate);
    void deleteById(Long id);
    void update(); // TODO: 07.03.2022
    InstructorEntity save(InstructorDto instructor);

}
