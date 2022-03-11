package com.example.project.service;

import com.example.project.dto.InstructorDto;
import com.example.project.entity.InstructorEntity;

import java.time.LocalDate;
import java.util.List;

public interface InstructorService {

    InstructorDto getById(Long id);
    List<InstructorDto> getByFullName(String firstName, String lastName);
    void deleteById(Long id);
    void update(); // TODO: 07.03.2022
    InstructorEntity save(InstructorDto instructor);
    InstructorDto getByPassport(String passport);
    List<InstructorDto> getAllActive();
    List<InstructorDto> getAll();

}
