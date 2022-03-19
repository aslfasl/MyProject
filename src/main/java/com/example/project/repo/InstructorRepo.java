package com.example.project.repo;

import com.example.project.entity.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorRepo extends JpaRepository<InstructorEntity, Long> {
    List<InstructorEntity> getInstructorEntityByFirstNameAndLastName(String firstName,
                                                                     String lastName);
    InstructorEntity findByPassport(String passport);
    boolean existsByPassport(String passport);
    List<InstructorEntity> findAllByIsActiveTrue();
    

}
