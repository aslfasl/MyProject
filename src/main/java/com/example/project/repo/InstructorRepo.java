package com.example.project.repo;

import com.example.project.entity.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InstructorRepo extends JpaRepository<InstructorEntity, Long> {
    InstructorEntity getInstructorEntityByFirstNameAndLastNameAndBirthdate(String firstName,
                                                                            String lastName,
                                                                            LocalDate birthdate);

}
