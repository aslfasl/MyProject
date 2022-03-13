package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.Converter;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutEntity;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class WorkoutServiceImpTest {

    @Autowired
    Converter converter;

    @Autowired
    WorkoutRepo workoutRepo;

    @Autowired
    WorkoutServiceImp service;

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
        workout.addInstructor(new InstructorEntity("one1", "one1", "00001", true, LocalDate.of(2001, 2, 2)));
        workout.addInstructor(new InstructorEntity("one11", "one11", "100001", true, LocalDate.of(2001, 2, 2)));
        workoutRepo.save(workout);
        long id = workout.getId();

        WorkoutDto workoutDto = service.getById(id);

        assertEquals(workout.getName(), workoutDto.getName());
        assertEquals(2, workoutDto.getClients().size());
        assertEquals(2, workoutDto.getInstructors().size());
        assertEquals(workout.getClients().size(), workoutDto.getClients().size());
        assertEquals(workout.getDurationInMinutes(), workoutDto.getDurationInMinutes());
        assertEquals(workout.getPeopleLimit(), workoutDto.getPeopleLimit());
    }

    @Test
    void shouldSaveWorkoutToDatabase() {
        String name = "TestWorkout";
        int durationInMin = 60;
        int peopleLimit = 10;
        WorkoutDto workoutDto =
                new WorkoutDto(name, durationInMin, true, peopleLimit, new HashSet<>(), new HashSet<>());
        assertFalse(workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, durationInMin, peopleLimit));

        service.save(workoutDto);

        assertTrue(workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, durationInMin, peopleLimit));
    }

    @Test
    void shouldGetWorkoutByName() {
        String name = "YOGA BY SOMEONE";
        WorkoutEntity workoutEntity = new WorkoutEntity(name, 90, true, 100);
        workoutRepo.save(workoutEntity);

        WorkoutDto workoutDto = service.getByName(name);

        assertEquals(workoutEntity.getName(), workoutDto.getName());
        assertEquals(workoutEntity.getDurationInMinutes(), workoutDto.getDurationInMinutes());
    }

    @Test
    @Transactional
    void shouldChangeAvailableInWorkoutWhenDeleteById() {
        WorkoutEntity workoutEntity = new WorkoutEntity("name", 90, true, 100);
        workoutRepo.save(workoutEntity);
        Long id = workoutEntity.getId();

        service.deleteById(id);

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

        assertEquals(2, service.getAllAvailable().size());
        assertEquals(3, service.getAll().size());
    }

    @Test
    @Transactional
    void shouldAddClientToWorkoutByWorkoutId() {
        WorkoutEntity workoutEntity = new WorkoutEntity("TestName", 90, true, 10);
        workoutRepo.save(workoutEntity);
        long id = workoutEntity.getId();
        ClientDto clientDto =
                new ClientDto("Poly", "Gaz", "d;;lkj", LocalDate.of(1995, 2, 14), true, new HashSet<>());
        assertEquals(0, workoutEntity.getClients().size());

        service.addClientToWorkoutById(clientDto, id);

        assertTrue(workoutRepo.getById(id).getClients().contains(converter.convertClientDto(clientDto)));
        assertEquals(1, workoutEntity.getClients().size());
    }

    @Test
    @Transactional
    void shouldAddInstructorToWorkoutByWorkoutId() {
        WorkoutEntity workoutEntity = new WorkoutEntity("TestName22", 90, true, 10);
        workoutRepo.save(workoutEntity);
        long id = workoutEntity.getId();
        InstructorDto instructorDto =
                new InstructorDto("Igor", "Komarov", "ddeqqw", true,
                        LocalDate.of(1955, 1,1),
                        new HashSet<>());
        assertEquals(0, workoutEntity.getInstructors().size());

        service.addInstructorToWorkoutById(instructorDto, id);

        assertTrue(workoutRepo.getById(id).getInstructors().contains(converter.convertInstructorDto(instructorDto)));
        assertEquals(1, workoutEntity.getInstructors().size());
    }
}