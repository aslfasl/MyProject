package com.example.project.service;

import com.example.project.dto.InstructorDto;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.time.LocalDate;
import java.util.List;

public interface InstructorService {

    InstructorDto getById(Long id);

    List<InstructorDto> getByFullName(String firstName, String lastName);

    InstructorDto deleteById(Long id);

    InstructorDto updateById(Long id, InstructorDto instructorDto) throws JsonMappingException;

    InstructorDto save(InstructorDto instructor);

    InstructorDto getByPassport(String passport);

    List<InstructorDto> getAllActive();

    List<InstructorDto> getAll();


}
