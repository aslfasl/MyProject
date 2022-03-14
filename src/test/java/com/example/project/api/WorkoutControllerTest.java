package com.example.project.api;

import com.example.project.dto.WorkoutDto;
import com.example.project.entity.WorkoutEntity;
import com.example.project.repo.WorkoutRepo;
import com.example.project.service.WorkoutService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class WorkoutControllerTest {

    @Autowired
    WorkoutRepo workoutRepo;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @AfterEach
    void clean() {
        workoutRepo.deleteAll();
    }

    @Test
    void shouldGetWorkoutById() throws Exception {
        WorkoutEntity workoutEntity =
                new WorkoutEntity("some sport", 60, true, 10);
        workoutRepo.save(workoutEntity);
        long id = workoutEntity.getId();

        mockMvc.perform(get("/api/workout/by_id/" + id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", equalTo("some sport")))
                .andExpect(jsonPath("$.durationInMinutes", equalTo(60)))
                .andExpect(jsonPath("$.peopleLimit", equalTo(10)));
    }

    @Test
    void shouldSaveClientToDatabase() throws Exception {
        String name = "dto save test";
        int duration = 90;
        int limit = 5;
        WorkoutDto workoutDto =
                new WorkoutDto(name, duration, true, limit, new HashSet<>(), new HashSet<>());
        assertFalse(workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, duration, limit));

        mockMvc.perform(post("/api/workout/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.durationInMinutes", equalTo(duration)))
                .andExpect(jsonPath("$.peopleLimit", equalTo(limit)));
        assertTrue(workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, duration, limit));
    }

    @Test
    void shouldGetWorkoutByName() throws Exception {
        String name = "workout123";
        WorkoutEntity workoutEntity =
                new WorkoutEntity(name, 30, true, 15);
        assertFalse(workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, 30, 15));
        workoutRepo.save(workoutEntity);

        mockMvc.perform(get("/api/workout/by_name?name={name}", name))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.durationInMinutes", equalTo(30)))
                .andExpect(jsonPath("$.peopleLimit", equalTo(15)));
    }

    @Test
    @Transactional
    void shouldMakeWorkoutUnavailableWhenDeleteById() throws Exception {
        WorkoutEntity workoutEntity =
                new WorkoutEntity("123name", 30, true, 15);
        workoutRepo.save(workoutEntity);
        long id = workoutEntity.getId();

        mockMvc.perform(post("/api/workout/delete/" + id))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.name", equalTo("123name")))
                .andExpect(jsonPath("$.available", equalTo(false)));
        assertFalse(workoutRepo.getById(id).isAvailable());
    }

    @Test
    void shouldGetListOfAllAvailableWorkouts() throws Exception {
        WorkoutEntity workoutEntity1 =
                new WorkoutEntity("workout1", 30, true, 15);
        WorkoutEntity workoutEntity2 =
                new WorkoutEntity("workout11", 44, true, 15);
        WorkoutEntity workoutEntity3 =
                new WorkoutEntity("workout111", 22, false, 15);
        workoutRepo.save(workoutEntity1);
        workoutRepo.save(workoutEntity2);
        workoutRepo.save(workoutEntity3);

        mockMvc.perform(get("/api/workout/available"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, true)));
    }

    @Test
    void shouldGetListOfAllWorkouts() throws Exception {
        WorkoutEntity workoutEntity1 =
                new WorkoutEntity("workout1", 30, true, 15);
        WorkoutEntity workoutEntity2 =
                new WorkoutEntity("workout11", 44, true, 15);
        WorkoutEntity workoutEntity3 =
                new WorkoutEntity("workout111", 22, false, 15);
        workoutRepo.save(workoutEntity1);
        workoutRepo.save(workoutEntity2);
        workoutRepo.save(workoutEntity3);

        mockMvc.perform(get("/api/workout/all"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, true, false)));
    }
}