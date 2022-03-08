package com.example.project.service;

import com.example.project.entity.InstructorEntity;

import java.time.LocalDate;

public interface InstructorService {

    InstructorEntity getById(Long id);
    InstructorEntity getByFullNameAndBirthdate(String firstName, String lastName, LocalDate birthdate);
    void deleteById(Long id);
    void update(); // TODO: 07.03.2022
    InstructorEntity save(InstructorEntity instructor); // TODO: 07.03.2022  

}
