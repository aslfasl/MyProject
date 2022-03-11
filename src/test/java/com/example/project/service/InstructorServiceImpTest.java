package com.example.project.service;

import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.repo.InstructorRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InstructorServiceImpTest {

    @Autowired
    private InstructorRepo instructorRepo;

    @Autowired
    private InstructorServiceImp service;

    @AfterEach
    void after() {
        instructorRepo.deleteAll();
    }

    @Test
    @Transactional
    void shouldGetInstructorById() {
        WorkoutEntity workoutEntity = new WorkoutEntity("asd", 12, true);
        InstructorEntity instructorEntity =
                new InstructorEntity("testName",
                        "testSurname",
                        "1234",
                        true,
                        LocalDate.of(2000, 1, 1));
        instructorEntity.addWorkout(workoutEntity);
        instructorRepo.save(instructorEntity);
        long id = instructorEntity.getId();

        InstructorDto instructorDto = service.getById(id);

        assertEquals(instructorEntity.getPassport(), instructorDto.getPassport());
        assertEquals(instructorEntity.getBirthdate(), instructorDto.getBirthdate());
        assertEquals(instructorEntity.getFirstName(), instructorDto.getFirstName());
        assertEquals(instructorEntity.getLastName(), instructorDto.getLastName());
        assertEquals(instructorEntity.getInstructorWorkouts().size(),
                instructorDto.getInstructorWorkouts().size());
    }

    @Test
    void shouldGetAllInstructorsByFullName() {
        String firstName = "InstructorName1";
        String lastName = "InstructorName1";
        InstructorEntity instructorEntity1 =
                new InstructorEntity(firstName, lastName,";lkj",true, LocalDate.of(2000, 1, 1));
        InstructorEntity instructorEntity2 =
                new InstructorEntity(firstName, lastName,"x2xx2xj",true, LocalDate.of(2000, 1, 1));
        instructorRepo.save(instructorEntity1);
        instructorRepo.save(instructorEntity2);

        List<InstructorDto> instructorsWithTheSameName = service.getByFullName(firstName, lastName);
        assertTrue(instructorsWithTheSameName.stream()
                .allMatch(instructorDto -> instructorDto.getFirstName().equalsIgnoreCase(firstName)));
        assertTrue(instructorsWithTheSameName.stream()
                .allMatch(instructorDto -> instructorDto.getLastName().equalsIgnoreCase(lastName)));
    }

    @Test
    @Transactional
    void shouldChangeActiveToFalseWhenDeleteById() {
        InstructorEntity instructorEntity = new InstructorEntity();
        instructorEntity.setActive(true);
        instructorRepo.save(instructorEntity);
        long id = instructorEntity.getId();

        service.deleteById(id);

        assertFalse(instructorRepo.getById(id).isActive());
    }

    @Test
    void update() {
        // TODO: 07.03.2022
    }

    @Test
    void save() {
        String passport = "111160";
        InstructorDto instructorDto =
                new InstructorDto("Bob", "Smith", passport, true,
                        LocalDate.of(2000, 1,1), null);
        Set<WorkoutDto> workoutDtoSet = new HashSet<>();
        instructorDto.setInstructorWorkouts(workoutDtoSet);
        assertFalse(instructorRepo.existsByPassport(passport));

        InstructorEntity instructorEntity = service.save(instructorDto);


        assertTrue(instructorRepo.existsByPassport(passport));
        assertEquals(instructorDto.getPassport(), instructorEntity.getPassport());
        assertEquals(instructorDto.getFirstName(), instructorEntity.getFirstName());
        assertEquals(instructorDto.getBirthdate(), instructorEntity.getBirthdate());
    }

    @Test
    void shouldGetInstructorByPassport() {
        String passport = "4009";
        InstructorEntity instructorEntity = new InstructorEntity(
                "Alex", "Boch", passport, true,
                LocalDate.of(1989, 1, 1));
        WorkoutEntity workoutEntity = new WorkoutEntity("crossfit", 45, true);
        instructorEntity.addWorkout(workoutEntity);
        instructorRepo.save(instructorEntity);

        InstructorDto instructorDto = service.getByPassport(passport);

        assertEquals(instructorEntity.getInstructorWorkouts().size(),
                instructorDto.getInstructorWorkouts().size());
        assertEquals(instructorEntity.getPassport(), instructorDto.getPassport());
        assertEquals(instructorEntity.getLastName(), instructorDto.getLastName());
    }

    @Test
    void shouldGetAllandAllActive() {
        InstructorEntity instructorEntity1 = new InstructorEntity();
        InstructorEntity instructorEntity2 = new InstructorEntity();
        InstructorEntity instructorEntity3 = new InstructorEntity();
        instructorEntity1.setPassport("1ooo");
        instructorEntity2.setPassport("2ooo");
        instructorEntity3.setPassport("3ooo");
        instructorEntity3.setActive(true);
        assertEquals(0, instructorRepo.findAll().size());

        instructorRepo.save(instructorEntity1);
        instructorRepo.save(instructorEntity2);
        instructorRepo.save(instructorEntity3);

        assertEquals(3, service.getAll().size());
        assertEquals(1, service.getAllActive().size());
    }
}