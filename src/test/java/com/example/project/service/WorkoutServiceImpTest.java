package com.example.project.service;

import com.example.project.dto.Converter;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.exception.CustomException;
import com.example.project.repo.ClientRepo;
import com.example.project.repo.InstructorRepo;
import com.example.project.repo.WorkoutRepo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class WorkoutServiceImpTest {

    @Autowired
    Converter converter;

    @Autowired
    WorkoutRepo workoutRepo;

    @Autowired
    WorkoutServiceImp workoutService;

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    InstructorRepo instructorRepo;

    @BeforeEach
    void setUp() {
        workoutRepo.deleteAll();
        clientRepo.deleteAll();
        instructorRepo.deleteAll();
    }

    @Test
    @Transactional
    void getById() {
        WorkoutEntity workout = new WorkoutEntity("testOne", 60, true, 100);
        workout.addClient(new ClientEntity("one", "one", "101010", LocalDate.of(2000, 1, 1), true));
        workout.addClient(new ClientEntity("one22", "one22", "10122010", LocalDate.of(2000, 1, 1), true));
        workout.addInstructor(new InstructorEntity("one1", "one1", "00001", LocalDate.of(2001, 2, 2), true));
        workout.addInstructor(new InstructorEntity("one11", "one11", "100001", LocalDate.of(2001, 2, 2), true));
        workoutRepo.save(workout);
        long id = workout.getId();

        WorkoutDto workoutDto = workoutService.getById(id);

        assertEquals(workout.getName(), workoutDto.getName());
        assertEquals(2, workoutDto.getClients().size());
        assertEquals(2, workoutDto.getInstructors().size());
        assertEquals(workout.getClients().size(), workoutDto.getClients().size());
        assertEquals(workout.getDurationInMinutes(), workoutDto.getDurationInMinutes());
        assertEquals(workout.getPeopleLimit(), workoutDto.getPeopleLimit());
    }

    @Test
    void shouldThrowCustomExceptionWhenGetById() {
        long workoutId = -11;
        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.getById(workoutId));
        assertEquals("Workout with id " + workoutId + " not found", exception.getMessage());
    }

    @Test
    void shouldSaveWorkoutToDatabase() {
        String name = "TestWorkout";
        int durationInMin = 60;
        int peopleLimit = 10;
        WorkoutDto workoutDto =
                new WorkoutDto(name, durationInMin, true, peopleLimit, new HashSet<>(), new HashSet<>());
        assertFalse(workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, durationInMin, peopleLimit));

        workoutService.save(workoutDto);

        assertTrue(workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, durationInMin, peopleLimit));
    }

    @Test
    void shouldThrowCustomExceptionWhenTryingToSaveAlreadyExistingWorkout() {
        WorkoutEntity workoutEntity = new WorkoutEntity("some workout", 99, true, 10);
        workoutRepo.save(workoutEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.save(converter.convertWorkoutEntity(workoutEntity)));

        assertEquals("Workout " + workoutEntity.getName() + " already exists", exception.getMessage());
    }

    @Test
    void shouldGetWorkoutByName() {
        String name = "YOGA BY SOMEONE";
        WorkoutEntity workoutEntity = new WorkoutEntity(name, 90, true, 100);
        workoutRepo.save(workoutEntity);

        WorkoutDto workoutDto = workoutService.getByName(name);

        assertEquals(workoutEntity.getName(), workoutDto.getName());
        assertEquals(workoutEntity.getDurationInMinutes(), workoutDto.getDurationInMinutes());
    }

    @Test
    void shouldThrowCustomExceptionWhenGetByName() {
        String name = "YOGA BY SOMEONE";

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.getByName(name));

        assertEquals("Workout with name " + name + " not found", exception.getMessage());
    }

    @Test
    @Transactional
    void shouldChangeAvailableInWorkoutWhenDeleteById() {
        WorkoutEntity workoutEntity = new WorkoutEntity("name", 90, true, 100);
        workoutRepo.save(workoutEntity);
        Long id = workoutEntity.getId();

        workoutService.deleteById(id);

        assertFalse(workoutRepo.getById(id).isAvailable());
    }

    @Test
    void shouldGetAllAvailableAndAllWorkouts() {
        WorkoutEntity workoutEntity1 = new WorkoutEntity();
        WorkoutEntity workoutEntity2 = new WorkoutEntity();
        WorkoutEntity workoutEntity3 = new WorkoutEntity();
        workoutEntity1.setAvailable(true);
        workoutEntity2.setAvailable(true);
        workoutEntity3.setAvailable(false);
        assertEquals(0, workoutRepo.findAll().size());
        workoutRepo.save(workoutEntity1);
        workoutRepo.save(workoutEntity2);
        workoutRepo.save(workoutEntity3);

        assertEquals(2, workoutService.getAllAvailable().size());
        assertEquals(3, workoutService.getAll().size());
    }

    @Test
    @Transactional
    void shouldAddClientToWorkoutByWorkoutNameAndClientId() {
        String workoutName = "TestName";
        WorkoutEntity workoutEntity = new WorkoutEntity(workoutName, 90, true, 10);
        workoutRepo.save(workoutEntity);
        long workoutId = workoutEntity.getId();
        ClientEntity clientEntity =
                new ClientEntity("Poly", "Gaz", "d;;lkj", LocalDate.of(1995, 2, 14), true);
        assertEquals(0, workoutEntity.getClients().size());
        clientRepo.save(clientEntity);
        long clientId = clientEntity.getId();

        workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, clientId);


        WorkoutEntity savedWorkout = workoutRepo.getById(workoutId);
        assertTrue(savedWorkout.getClients().contains(clientEntity));
        assertEquals(1, savedWorkout.getClients().size());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddClientToWorkoutWithWrongName() {
        String workoutName = "TestName";
        long id = -123;

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, id));

        assertEquals("Workout with name " + workoutName + " not found", exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddClientToWorkoutWithWrongId() {
        String workoutName = "TestName";
        WorkoutEntity workoutEntity = new WorkoutEntity(workoutName, 90, true, 10);
        workoutRepo.save(workoutEntity);
        long id = -123;

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, id));

        assertEquals("Client with id " + id + " not found", exception.getMessage());
    }


    @Test
    @Transactional
    void shouldAddInstructorToWorkoutByWorkoutNameAndInstructorId() {
        String workoutName = "TestName22";
        WorkoutEntity workoutEntity = new WorkoutEntity(workoutName, 90, true, 10);
        workoutRepo.save(workoutEntity);
        long workoutId = workoutEntity.getId();
        InstructorEntity instructorEntity =
                new InstructorEntity("Igor", "Komarov", "ddeqqw",
                        LocalDate.of(1955, 1, 1), true);
        instructorRepo.save(instructorEntity);
        long instructorId = instructorEntity.getId();

        workoutService.addInstructorToWorkoutByWorkoutNameAndInstructorId(workoutName, instructorId);

        WorkoutEntity savedWorkout = workoutRepo.getById(workoutId);
        assertTrue(savedWorkout.getInstructors().contains(instructorEntity));
        assertEquals(1, savedWorkout.getInstructors().size());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddInstructorToWorkoutWithWrongName() {
        String workoutName = "TestName";
        long id = -123;

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addInstructorToWorkoutByWorkoutNameAndInstructorId(workoutName, id));

        assertEquals("Workout with name " + workoutName + " not found", exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddInstructorToWorkoutWithWrongId() {
        String workoutName = "TestName";
        WorkoutEntity workoutEntity = new WorkoutEntity(workoutName, 90, true, 10);
        workoutRepo.save(workoutEntity);
        long id = -123;

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addInstructorToWorkoutByWorkoutNameAndInstructorId(workoutName, id));

        assertEquals("Instructor with id " + id + " not found", exception.getMessage());
    }
}