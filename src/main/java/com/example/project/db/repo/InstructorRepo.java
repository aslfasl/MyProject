package com.example.project.db.repo;

import com.example.project.db.entity.InstructorEntity;
import com.example.project.db.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface InstructorRepo extends JpaRepository<InstructorEntity, Long> {
    InstructorEntity getInstructorEntityByFirstNameAndLastNameAndBirthdate(String firstName,
                                                                            String lastName,
                                                                            LocalDate birthdate);

}
