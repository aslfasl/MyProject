package com.example.project.service;

import com.example.project.db.entity.InstructorEntity;
import com.example.project.db.repo.InstructorRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Transactional
public class InstructorServiceImp implements InstructorService{

    private final InstructorRepo instructorRepo;

    @Override
    public InstructorEntity getById(Long id) {
        return instructorRepo.getById(id);
    }

    @Override
    public InstructorEntity getByFullNameAndBirthdate(String firstName, String lastName, LocalDate birthdate) {
        return instructorRepo.getInstructorEntityByFirstNameAndLastNameAndBirthdate(firstName, lastName, birthdate);
    }

    @Override
    public void deleteById(Long id) {
// TODO: 07.03.2022
    }

    @Override
    public void update() {
// TODO: 07.03.2022
    }

    @Override
    public InstructorEntity save(InstructorEntity instructor) {
        // TODO: 07.03.2022
        return null;
    }
}
