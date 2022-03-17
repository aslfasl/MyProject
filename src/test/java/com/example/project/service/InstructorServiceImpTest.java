package com.example.project.service;

import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.exception.CustomException;
import com.example.project.repo.InstructorRepo;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InstructorServiceImpTest {

    @Autowired
    private InstructorRepo instructorRepo;

    @Autowired
    private InstructorServiceImp instructorService;

    @AfterEach
    void after() {
        instructorRepo.deleteAll();
    }

    @Test
    void shouldAddWorkoutToInstructorEntity() {
        InstructorEntity instructorEntity =
                new InstructorEntity("Jack", "Dogson", "890123",
                        LocalDate.of(1989, 1, 1), true);
        WorkoutEntity workoutEntity = new WorkoutEntity("circle running", 999, true, 100);
        assertFalse(instructorEntity.getInstructorWorkouts().contains(workoutEntity));

        instructorService.addWorkoutToInstructor(workoutEntity, instructorEntity);

        assertTrue(instructorEntity.getInstructorWorkouts().contains(workoutEntity));
    }

    @Test
    void shouldThrowCustomExceptionWhenAddWorkoutInSecondTime() {
        InstructorEntity instructorEntity =
                new InstructorEntity("Jack", "Dogson", "890123",
                        LocalDate.of(1989, 1, 1), true);
        assertEquals(0, instructorEntity.getInstructorWorkouts().size());
        WorkoutEntity workoutEntity = new WorkoutEntity("circle running", 999, true, 100);
        instructorService.addWorkoutToInstructor(workoutEntity, instructorEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> instructorService.addWorkoutToInstructor(workoutEntity, instructorEntity));

        assertEquals("This instructor already signed for: " + workoutEntity.getName(), exception.getMessage());
    }

    @Test
    @Transactional
    void shouldGetInstructorById() {
        WorkoutEntity workoutEntity = new WorkoutEntity("asd", 12, true, 100);
        InstructorEntity instructorEntity =
                new InstructorEntity("testName", "testSurname", "1234",
                        LocalDate.of(2000, 1, 1), true);
        instructorService.addWorkoutToInstructor(workoutEntity, instructorEntity);
        instructorRepo.save(instructorEntity);
        long id = instructorEntity.getId();

        InstructorDto instructorDto = instructorService.getById(id);

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
                new InstructorEntity(firstName, lastName, ";lkj", LocalDate.of(2000, 1, 1), true);
        InstructorEntity instructorEntity2 =
                new InstructorEntity(firstName, lastName, "x2xx2xj", LocalDate.of(2000, 1, 1), true);
        instructorRepo.save(instructorEntity1);
        instructorRepo.save(instructorEntity2);

        List<InstructorDto> instructorsWithTheSameName = instructorService.getByFullName(firstName, lastName);
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

        instructorService.deleteById(id);

        assertFalse(instructorRepo.getById(id).isActive());
    }

    @Test
    @Transactional
    void shouldUpdateInstructorById() throws JsonMappingException {
        InstructorEntity instructorEntity =
                new InstructorEntity("testName", "testSurname", "1234",
                        LocalDate.of(2000, 1, 1), true);
        WorkoutEntity workoutEntity = new WorkoutEntity("basketball", 45, true, 12);
        instructorService.addWorkoutToInstructor(workoutEntity, instructorEntity);
        instructorRepo.save(instructorEntity);
        long id = instructorEntity.getId();
        String newFirstname = "Anna", newLastname = "Ivanova", newPassport = "fffda123";
        LocalDate newBirthdate = LocalDate.of(1995, 5, 5);
        boolean newActive = true;


        instructorService.updateById(id, newFirstname, newLastname, newPassport, newBirthdate, newActive);

        InstructorEntity instructorSaved = instructorRepo.getById(id);
        assertEquals(newFirstname, instructorSaved.getFirstName());
        assertEquals(newLastname, instructorSaved.getLastName());
        assertEquals(newPassport, instructorSaved.getPassport());
        assertEquals(newBirthdate, instructorSaved.getBirthdate());
        assertEquals(newActive, instructorSaved.isActive());
        assertEquals(instructorEntity.getInstructorWorkouts().size(), instructorSaved.getInstructorWorkouts().size());
        assertTrue(instructorSaved.getInstructorWorkouts().contains(workoutEntity));
    }

    @Test
    void save() {
        String passport = "111160";
        InstructorDto instructorDto =
                new InstructorDto("Bob", "Smith", passport, true,
                        LocalDate.of(2000, 1, 1), null);
        Set<WorkoutDto> workoutDtoSet = new HashSet<>();
        instructorDto.setInstructorWorkouts(workoutDtoSet);
        assertFalse(instructorRepo.existsByPassport(passport));

        InstructorDto savedInstructor = instructorService.save(instructorDto);


        assertTrue(instructorRepo.existsByPassport(passport));
        assertEquals(instructorDto.getPassport(), savedInstructor.getPassport());
        assertEquals(instructorDto.getFirstName(), savedInstructor.getFirstName());
        assertEquals(instructorDto.getBirthdate(), savedInstructor.getBirthdate());
    }

    @Test
    void shouldGetInstructorByPassport() {
        String passport = "4009";
        InstructorEntity instructorEntity = new InstructorEntity(
                "Alex", "Boch", passport,
                LocalDate.of(1989, 1, 1), true);
        WorkoutEntity workoutEntity = new WorkoutEntity("crossfit", 45, true, 100);
        instructorService.addWorkoutToInstructor(workoutEntity, instructorEntity);
        instructorRepo.save(instructorEntity);

        InstructorDto instructorDto = instructorService.getByPassport(passport);

        assertEquals(instructorEntity.getInstructorWorkouts().size(),
                instructorDto.getInstructorWorkouts().size());
        assertEquals(instructorEntity.getPassport(), instructorDto.getPassport());
        assertEquals(instructorEntity.getLastName(), instructorDto.getLastName());
    }

    @Test
    void shouldGetAllAndAllActive() {
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

        assertEquals(3, instructorService.getAll().size());
        assertEquals(1, instructorService.getAllActive().size());
    }
}