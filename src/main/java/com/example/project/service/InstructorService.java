package com.example.project.service;

import com.example.project.dto.InstructorDto;

import java.util.List;

public interface InstructorService {

    InstructorDto getById(Long id);
    List<InstructorDto> getByFullName(String firstName, String lastName);
    InstructorDto deleteById(Long id);
    void update(); // TODO: 07.03.2022
    InstructorDto save(InstructorDto instructor);
    InstructorDto getByPassport(String passport);
    List<InstructorDto> getAllActive();
    List<InstructorDto> getAll();

}
