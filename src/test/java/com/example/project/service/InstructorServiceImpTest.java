package com.example.project.service;

import com.example.project.dto.*;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutClassEntity;
import com.example.project.exception.CustomException;
import com.example.project.repo.InstructorRepo;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.project.exception.ExceptionMessageUtils.*;
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
        InstructorEntity instructorEntity = new InstructorEntity("Jack",
                "Dogson",
                "890123",
                LocalDate.of(1989, 1, 1),
                true);
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity(
                "circle running",
                true,
                100);
        assertFalse(instructorEntity.getInstructorWorkouts().contains(workoutClassEntity));

        instructorService.addWorkoutToInstructor(workoutClassEntity, instructorEntity);

        assertTrue(instructorEntity.getInstructorWorkouts().contains(workoutClassEntity));
    }

    @Test
    void shouldThrowCustomExceptionWhenAddWorkoutInSecondTime() {
        InstructorEntity instructorEntity = new InstructorEntity("Jack",
                "Dogson",
                "890123",
                LocalDate.of(1989, 1, 1),
                true);
        assertEquals(0, instructorEntity.getInstructorWorkouts().size());
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("circle running", true, 100);
        instructorService.addWorkoutToInstructor(workoutClassEntity, instructorEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> instructorService.addWorkoutToInstructor(workoutClassEntity, instructorEntity));

        assertEquals("This instructor already signed for: " +
                workoutClassEntity.getName(), exception.getMessage());
    }

    @Test
    @Transactional
    void shouldGetInstructorById() {
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("asd", true, 100);
        InstructorEntity instructorEntity = new InstructorEntity("testName",
                "testSurname",
                "1234",
                LocalDate.of(2000, 1, 1),
                true);
        instructorService.addWorkoutToInstructor(workoutClassEntity, instructorEntity);
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
    @Transactional
    void shouldThrowCustomExceptionWhenGetInstructorByIdWithWrongId() {
        long id = -90L;

        CustomException exception = assertThrows(CustomException.class, () -> instructorService.getById(id));

        assertEquals(INSTRUCTOR_NOT_FOUND_ID + id, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldThrowCustomExceptionWhenSaveInstructorWithPassportAlreadyExists() {
        InstructorEntity instructorEntity = new InstructorEntity("testName",
                "testSurname",
                "1234",
                LocalDate.of(2000, 1, 1),
                true);
        instructorRepo.save(instructorEntity);
        InstructorDto instructorDto = new InstructorDto(null,
                "test",
                "test",
                "1234",
                "address",
                "123456",
                "speciality",
                "education",
                true,
                LocalDate.of(2000, 1, 1),
                new HashSet<>());

        CustomException exception = assertThrows(CustomException.class, () -> instructorService.save(instructorDto));

        assertEquals(INSTRUCTOR_ALREADY_EXISTS_PASSPORT + instructorDto.getPassport(), exception.getMessage());
    }

    @Test
    void shouldGetAllInstructorsByFullName() {
        String firstName = "InstructorName1";
        String lastName = "InstructorName1";
        InstructorEntity instructorEntity1 = new InstructorEntity(firstName,
                lastName,
                ";lkj",
                LocalDate.of(2000, 1, 1),
                true);
        InstructorEntity instructorEntity2 = new InstructorEntity(firstName,
                lastName,
                "x2xx2xj",
                LocalDate.of(2000, 1, 1),
                true);
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
        InstructorEntity instructorEntity = new InstructorEntity("testName",
                "testSurname",
                "1234",
                LocalDate.of(2000, 1, 1),
                true);
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("basketball", true, 12);
        instructorService.addWorkoutToInstructor(workoutClassEntity, instructorEntity);
        instructorRepo.save(instructorEntity);
        long id = instructorEntity.getId();
        String newFirstname = "Anna", newLastname = "Ivanova", newPassport = "fffda123",
                newAddress = "address", newPhone = "123456", newSpeciality = "speciality", newEducation = "education";
        LocalDate newBirthdate = LocalDate.of(1995, 5, 5);
        boolean newActive = true;
        InstructorDto instructorDto = new InstructorDto(null,
                newFirstname,
                newLastname,
                newPassport,
                newAddress,
                newPhone,
                newSpeciality,
                newEducation,
                newActive,
                newBirthdate,
                null);


        instructorService.updateById(id, instructorDto);

        InstructorEntity instructorSaved = instructorRepo.getById(id);
        assertEquals(newFirstname, instructorSaved.getFirstName());
        assertEquals(newLastname, instructorSaved.getLastName());
        assertEquals(newPassport, instructorSaved.getPassport());
        assertEquals(newAddress, instructorSaved.getAddress());
        assertEquals(newPhone, instructorSaved.getPhone());
        assertEquals(newSpeciality, instructorSaved.getSpeciality());
        assertEquals(newEducation, instructorSaved.getEducation());
        assertEquals(newBirthdate, instructorSaved.getBirthdate());
        assertEquals(newActive, instructorSaved.isActive());
        assertEquals(instructorEntity.getInstructorWorkouts().size(), instructorSaved.getInstructorWorkouts().size());
        assertTrue(instructorSaved.getInstructorWorkouts().contains(workoutClassEntity));
    }

    @Test
    void shouldThrowCustomExceptionWhenUpdateByIdNotFoundById() {
        InstructorDto instructorDto = new InstructorDto();
        long id = -1224;

        CustomException exception =
                assertThrows(CustomException.class, () -> instructorService.updateById(id, instructorDto));

        assertEquals(CLIENT_NOT_FOUND_ID + id, exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenUpdateByIdPassportAlreadyExists() {
        InstructorEntity instructorEntity =
                new InstructorEntity("Jack", "White", "12", LocalDate.of(2000, 1, 2), true);
        instructorRepo.save(instructorEntity);
        long id = instructorEntity.getId();
        InstructorDto instructorDto =
                new InstructorDto(null, null, null, "12", null,
                        null, null, null, true, null, null);


        CustomException exception =
                assertThrows(CustomException.class, () -> instructorService.updateById(id, instructorDto));

        assertEquals(CLIENT_ALREADY_EXISTS_PASSPORT + instructorDto.getPassport(), exception.getMessage());
    }

    @Test
    void shouldSaveInstructor() {
        String passport = "111160";
        InstructorDto instructorDto = new InstructorDto(null,
                "Bob",
                "Smith",
                passport,
                "address",
                "123456",
                "speciality",
                "education",
                true,
                LocalDate.of(2000, 1, 1),
                null);
        Set<WorkoutClassDto> workoutClassDtoSet = new HashSet<>();
        instructorDto.setInstructorWorkouts(workoutClassDtoSet);
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
        InstructorEntity instructorEntity = new InstructorEntity("Alex",
                "Boch",
                passport,
                LocalDate.of(1989, 1, 1),
                true);
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("crossfit", true, 100);
        instructorService.addWorkoutToInstructor(workoutClassEntity, instructorEntity);
        instructorRepo.save(instructorEntity);

        InstructorDto instructorDto = instructorService.getByPassport(passport);

        assertEquals(instructorEntity.getInstructorWorkouts().size(),
                instructorDto.getInstructorWorkouts().size());
        assertEquals(instructorEntity.getPassport(), instructorDto.getPassport());
        assertEquals(instructorEntity.getLastName(), instructorDto.getLastName());
    }

    @Test
    void shouldThrowCustomExceptionWhenGetInstructorByPassportNotFound() {
        String passport = "4009";

        CustomException exception = assertThrows(CustomException.class, () -> instructorService.getByPassport(passport));

        assertEquals(INSTRUCTOR_NOT_FOUND_PASSPORT + passport, exception.getMessage());
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

    @Test
    void shouldGetFilteredAndSortedClients() {
        InstructorPage instructorPage = new InstructorPage();
        instructorPage.setPageNumber(0);
        instructorPage.setPageSize(5);
        instructorPage.setSortDirection(Sort.Direction.DESC);
        instructorPage.setSortBy("firstName");
        InstructorSearchCriteria instructorSearchCriteria = new InstructorSearchCriteria();
        instructorSearchCriteria.setFirstName("dre");
        instructorSearchCriteria.setLastName("lko");

        InstructorEntity instructorEntity1 = new InstructorEntity("Andrey", "Volkov",
                "passport", LocalDate.of(2000, 1, 1), true);
        InstructorEntity instructorEntity2 = new InstructorEntity("Odrei", "Bolkob",
                "passport1", LocalDate.of(2000, 1, 1), true);
        InstructorEntity instructorEntity3 = new InstructorEntity("Asdf", "Qwer",
                "passport2", LocalDate.of(2000, 1, 1), true);
        InstructorEntity instructorEntity4 = new InstructorEntity("5wefAndrey123123", "faVolkovfa",
                "passport3", LocalDate.of(2000, 1, 1), true);
        InstructorEntity instructorEntity5 = new InstructorEntity("1drey", "Volkov",
                "passport4", LocalDate.of(2000, 1, 1), true);
        InstructorEntity instructorEntity6 = new InstructorEntity("Andrey", "Volkov",
                "passport5", LocalDate.of(2000, 1, 1), true);
        InstructorEntity instructorEntity7 = new InstructorEntity("Andrey", "Volkov",
                "passport8", LocalDate.of(2000, 1, 1), true);
        instructorRepo.save(instructorEntity1);
        instructorRepo.save(instructorEntity2);
        instructorRepo.save(instructorEntity3);
        instructorRepo.save(instructorEntity4);
        instructorRepo.save(instructorEntity5);
        instructorRepo.save(instructorEntity6);
        instructorRepo.save(instructorEntity7);


        Page<InstructorDto> resultPage = instructorService.findAllWithFilters(instructorPage, instructorSearchCriteria);

        assertEquals(6, resultPage.getTotalElements());
        assertEquals(5, resultPage.getSize());
        assertEquals("Odrei", resultPage.getContent().get(0).getFirstName());
        assertEquals("Andrey", resultPage.getContent().get(1).getFirstName());
    }
}