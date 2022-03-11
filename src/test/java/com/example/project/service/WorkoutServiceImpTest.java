package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.repo.ClientRepo;
import com.example.project.repo.InstructorRepo;
import com.example.project.repo.WorkoutRepo;
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
class WorkoutServiceImpTest {

    @Autowired
    WorkoutRepo workoutRepo;

    @Autowired
    WorkoutServiceImp service;

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    InstructorRepo instructorRepo;

    @BeforeEach
    void setUp(){
        workoutRepo.deleteAll();
    }

    @Test
    @Transactional
    void getById() {
        WorkoutEntity workout = new WorkoutEntity("testOne", 60, true, 100);
// TODO: 07.03.2022  
        workout.addClient(new ClientEntity("one", "one", "101010", LocalDate.of(2000,1,1), true));
        workout.addInstructor(new InstructorEntity("one1", "one1", "00001", true,LocalDate.of(2001,2,2)));
        workoutRepo.save(workout);
        long id = workout.getId();
        WorkoutEntity workoutDb = workoutRepo.findById(id).get();
        System.out.println(workoutDb.getClients() + "    " + workoutDb.getInstructors());
    }

    @Test
    void shouldSaveWorkoutToDatabase() {
        WorkoutDto workoutDto = new WorkoutDto();
        String name = "TestWorkout";
        int durationInMin = 60;
        boolean available = true;
        Set<ClientDto> clients = new HashSet<>();
        Set<InstructorDto> instructors = new HashSet<>();
        workoutDto.setAvailable(available);
        workoutDto.setDurationInMinutes(durationInMin);
        workoutDto.setName(name);
        workoutDto.setClients(clients);
        workoutDto.setInstructors(instructors);
        assertFalse(workoutRepo.existsByNameAndDurationInMinutes(name, durationInMin));

        service.save(workoutDto);

        assertTrue(workoutRepo.existsByNameAndDurationInMinutes(name, durationInMin));
        // TODO: 07.03.2022
    }

    @Test
    void shouldGetWorkoutByName() {
        String name = "YOGA BY SOMEONE";
        WorkoutEntity workoutEntity = new WorkoutEntity(name, 90, true, 100);
        workoutRepo.save(workoutEntity);

        WorkoutDto workoutDto = service.getByName(name);

        assertEquals(workoutEntity.getName(), workoutDto.getName());
        assertEquals(workoutEntity.getDurationInMinutes(), workoutDto.getDurationInMinutes());
        // TODO: 07.03.2022  
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
        System.out.println(workoutRepo.findAll());
    }
}