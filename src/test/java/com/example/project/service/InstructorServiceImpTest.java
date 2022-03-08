package com.example.project.service;

import com.example.project.db.entity.InstructorEntity;
import com.example.project.db.entity.WorkoutEntity;
import com.example.project.db.repo.InstructorRepo;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InstructorServiceImpTest {

    @Autowired
    private InstructorRepo instructorRepo;

    @Autowired
    private InstructorServiceImp service;

    long id1;
    long id2;

    @BeforeEach
    void init() {
        InstructorEntity instructorEntity1 = new InstructorEntity("InstructorName1",
                "InstructorName1",
                LocalDate.of(2001, 1, 1));
        InstructorEntity instructorEntity2 = new InstructorEntity("InstructorName2",
                "InstructorName2",
                LocalDate.of(2002, 2, 2));
        instructorRepo.save(instructorEntity1);
        instructorRepo.save(instructorEntity2);
        id1 = instructorEntity1.getId();
        id2 = instructorEntity2.getId();
    }

    @AfterEach
    void after() {
        instructorRepo.deleteAll();
    }

    @Test
    void shouldGetInstructorById() {
        Optional<InstructorEntity> instructorById = instructorRepo.findById(id1);
        InstructorEntity instructorEntity = instructorById.get();
        System.out.println(instructorEntity);
        instructorEntity.addWorkout(new WorkoutEntity());
        instructorRepo.save(instructorEntity);
        id1 = instructorEntity.getId();
        instructorById = instructorRepo.findById(id1);
        instructorEntity = instructorById.get();
        System.out.println(instructorEntity.getInstructorWorkouts());
        // TODO: 07.03.2022
    }

    @Test
    void getByFullNameAndBirthdate() {
        InstructorEntity instructorEntity = instructorRepo.getInstructorEntityByFirstNameAndLastNameAndBirthdate("InstructorName1",
                "InstructorName1",
                LocalDate.of(2001, 1, 1));
        System.out.println(instructorEntity);
        System.out.println(instructorEntity.getInstructorWorkouts());
        // TODO: 07.03.2022
    }

    @Test
    void deleteById() {
        // TODO: 07.03.2022
    }

    @Test
    void update() {
        // TODO: 07.03.2022
    }
}