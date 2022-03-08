package com.example.project.service;

import com.example.project.entity.ClientEntity;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.repo.ClientRepo;
import com.example.project.repo.InstructorRepo;
import com.example.project.repo.WorkoutRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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


    @Test
    @Transactional
    void getById() {
        WorkoutEntity workout = new WorkoutEntity("testOne");
// TODO: 07.03.2022  
        workout.addClient(new ClientEntity("one", "one", LocalDate.of(2000,1,1), true));
        workout.addInstructor(new InstructorEntity("one1", "one1", LocalDate.of(2001,2,2)));
        workoutRepo.save(workout);
        long id = workout.getId();
        WorkoutEntity workoutDb = workoutRepo.findById(id).get();
        System.out.println(workoutDb.getClients() + "    " + workoutDb.getInstructors());
    }

    @Test
    void save() {
        // TODO: 07.03.2022  
    }

    @Test
    void getByName() {
        // TODO: 07.03.2022  
    }

    @Test
    void deleteById() {
        // TODO: 07.03.2022  
    }
}