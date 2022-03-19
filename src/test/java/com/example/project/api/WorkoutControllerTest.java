package com.example.project.api;

import com.example.project.dto.WorkoutDto;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.repo.WorkoutRepo;
import com.example.project.service.WorkoutServiceImp;
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
import java.time.LocalDate;
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
    WorkoutServiceImp workoutService;

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
                .andExpect(jsonPath("$.durationInMinutes", equalTo(60 * 90d)))
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
                .andExpect(jsonPath("$.durationInMinutes", equalTo(15 * 60d)))
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
                .andExpect(jsonPath("$.durationInMinutes", equalTo(90 * 60d)))
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
                .andExpect(jsonPath("$.durationInMinutes", equalTo(35 * 60d)))
                .andExpect(jsonPath("$.available", equalTo(false)))
                .andExpect(jsonPath("$.peopleLimit", equalTo(25)))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void shouldDeleteClientFromWorkoutById() throws Exception {
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Super workout", Duration.ofMinutes(30), true, 12);
        ClientEntity clientEntity =
                new ClientEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
        workoutService.addClientToWorkout(clientEntity, workoutEntity);
        workoutRepo.save(workoutEntity);
        long workoutId = workoutEntity.getId();
        long clientId = clientEntity.getId();

        mockMvc.perform((patch("/api/workout/delete_client?workoutId={workoutId}&clientId={clientId}", workoutId, clientId)))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", equalTo("A")))
                .andExpect(jsonPath("$.lastName", equalTo("B")))
                .andExpect(jsonPath("$.passport", equalTo("ccc")));
    }

    @Test
    void shouldDeleteInstructorFromWorkoutById() throws Exception {
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Super workout", Duration.ofMinutes(30), true, 12);
        InstructorEntity instructorEntity =
                new InstructorEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
        workoutService.addInstructorToWorkout(instructorEntity, workoutEntity);
        workoutRepo.save(workoutEntity);
        long workoutId = workoutEntity.getId();
        long instructorId = instructorEntity.getId();

        mockMvc.perform((patch("/api/workout/delete_instructor?workoutId={workoutId}&instructorId={instructorId}", workoutId, instructorId)))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", equalTo("A")))
                .andExpect(jsonPath("$.lastName", equalTo("B")))
                .andExpect(jsonPath("$.passport", equalTo("ccc")));
    }

    @Test
    void shouldGetAllActiveClientsFromWorkoutByWorkoutName() throws Exception {
        String name = "Jumping lower";
        ClientEntity clientEntity1 =
                new ClientEntity("Jim", "Bean", "15",
                        LocalDate.of(2000, 1, 1), true);
        ClientEntity clientEntity2 =
                new ClientEntity("Bill", "Bean", "154",
                        LocalDate.of(2000, 1, 1), true);
        ClientEntity clientEntity3 =
                new ClientEntity("Will", "Bean", "156",
                        LocalDate.of(2000, 1, 1), false);
        WorkoutEntity workoutEntity =
                new WorkoutEntity(name, Duration.ofMinutes(45), true, 5);
        workoutService.addClientToWorkout(clientEntity1, workoutEntity);
        workoutService.addClientToWorkout(clientEntity2, workoutEntity);
        workoutEntity.getClients().add(clientEntity3);
        clientEntity3.getClientWorkouts().add(workoutEntity);
        workoutRepo.save(workoutEntity);

        mockMvc.perform(get("/api/workout/active_clients?name={name}", name))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[*].active", containsInAnyOrder(true, true)));
    }

    @Test
    void shouldGetActiveInstructorsFromWorkoutByName() throws Exception {
        String name = "Jumping lower";
        InstructorEntity instructorEntity1 =
                new InstructorEntity("one1", "one1", "00001", LocalDate.of(2001, 2, 2), true);
        InstructorEntity instructorEntity2 =
                new InstructorEntity("one11", "one11", "100001", LocalDate.of(2001, 2, 2), true);
        InstructorEntity instructorEntity3 =
                new InstructorEntity("one111", "one111", "1000011", LocalDate.of(2001, 2, 2), false);
        WorkoutEntity workoutEntity =
                new WorkoutEntity(name, Duration.ofMinutes(45), true, 5);
        workoutService.addInstructorToWorkout(instructorEntity1, workoutEntity);
        workoutService.addInstructorToWorkout(instructorEntity2, workoutEntity);
        workoutEntity.getInstructors().add(instructorEntity3);
        instructorEntity3.getInstructorWorkouts().add(workoutEntity);
        workoutRepo.save(workoutEntity);

        mockMvc.perform(get("/api/workout/active_instructors?name={name}", name))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[*].active", containsInAnyOrder(true, true)));
    }
}