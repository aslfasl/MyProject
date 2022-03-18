package com.example.project.api;

import com.example.project.dto.WorkoutDto;
import com.example.project.entity.WorkoutEntity;
import com.example.project.repo.WorkoutRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashSet;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                new WorkoutEntity("some sport", Duration.ofMinutes(90), true, 10);
        workoutRepo.save(workoutEntity);
        long id = workoutEntity.getId();

        mockMvc.perform(get("/api/workout/by_id/" + id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", equalTo("some sport")))
                .andExpect(jsonPath("$.durationInMinutes", equalTo(60*90d)))
                .andExpect(jsonPath("$.peopleLimit", equalTo(10)));
    }

    @Test
    void shouldSaveClientToDatabase() throws Exception {
        String name = "dto save test";
        Duration duration = Duration.ofMinutes(15);
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
                .andExpect(jsonPath("$.durationInMinutes", equalTo(15*60d)))
                .andExpect(jsonPath("$.peopleLimit", equalTo(limit)));
        assertTrue(workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, duration, limit));
    }

    @Test
    void shouldGetWorkoutByName() throws Exception {
        String name = "workout123";
        WorkoutEntity workoutEntity =
                new WorkoutEntity(name, Duration.ofMinutes(90), true, 15);
        assertFalse(workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, Duration.ofMinutes(30), 15));
        workoutRepo.save(workoutEntity);

        mockMvc.perform(get("/api/workout/by_name?name={name}", name))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.durationInMinutes", equalTo(90*60d)))
                .andExpect(jsonPath("$.peopleLimit", equalTo(15)));
    }

    @Test
    @Transactional
    void shouldMakeWorkoutUnavailableWhenDeleteById() throws Exception {
        WorkoutEntity workoutEntity =
                new WorkoutEntity("123name", Duration.ofMinutes(90), true, 15);
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
                new WorkoutEntity("workout1", Duration.ofMinutes(90), true, 15);
        WorkoutEntity workoutEntity2 =
                new WorkoutEntity("workout11", Duration.ofMinutes(60), true, 15);
        WorkoutEntity workoutEntity3 =
                new WorkoutEntity("workout111", Duration.ofMinutes(40), false, 15);
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
                new WorkoutEntity("workout1", Duration.ofMinutes(40), true, 15);
        WorkoutEntity workoutEntity2 =
                new WorkoutEntity("workout11", Duration.ofMinutes(60), true, 15);
        WorkoutEntity workoutEntity3 =
                new WorkoutEntity("workout111", Duration.ofMinutes(90), false, 15);
        workoutRepo.save(workoutEntity1);
        workoutRepo.save(workoutEntity2);
        workoutRepo.save(workoutEntity3);

        mockMvc.perform(get("/api/workout/all"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, true, false)));
    }

    @Test
    void shouldUpdateWorkoutById() throws Exception {
        WorkoutEntity workoutEntity =
                new WorkoutEntity("workout1", Duration.ofMinutes(40), true, 15);
        workoutRepo.save(workoutEntity);
        long id = workoutEntity.getId();
        WorkoutDto workoutDto = new WorkoutDto("Dancing Queen", Duration.ofMinutes(35), false, 25, null, null);

        mockMvc.perform((patch("/api/workout/update?id={id}", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutDto)))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.name", equalTo("Dancing Queen")))
                .andExpect(jsonPath("$.durationInMinutes", equalTo(35*60d)))
                .andExpect(jsonPath("$.available", equalTo(false)))
                .andExpect(jsonPath("$.peopleLimit", equalTo(25)))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}