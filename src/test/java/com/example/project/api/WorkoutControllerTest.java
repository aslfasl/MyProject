package com.example.project.api;

import com.example.project.converter.Converter;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutClassDto;
import com.example.project.dto.WorkoutSessionDto;
import com.example.project.entity.*;
import com.example.project.repo.ClientRepo;
import com.example.project.repo.InstructorRepo;
import com.example.project.repo.WorkoutClassRepo;
import com.example.project.service.WorkoutClassServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
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
import java.time.LocalTime;
import java.util.HashSet;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "username", authorities = {"ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER"})
@RequiredArgsConstructor
class WorkoutControllerTest {

    @Autowired
    WorkoutClassRepo classRepo;

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    InstructorRepo instructorRepo;

    @Autowired
    WorkoutClassServiceImp workoutService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private Converter converter;

    @BeforeEach
    public void setup() {
        classRepo.deleteAll();
        clientRepo.deleteAll();
        instructorRepo.deleteAll();
    }

    @Test
    void shouldGetWorkoutClassById() throws Exception {
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("some sport", true, 10);
        classRepo.save(workoutClassEntity);
        long id = workoutClassEntity.getId();

        mockMvc.perform(get("/api/workout/by_id/" + id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", equalTo("some sport")))
                .andExpect(jsonPath("$.available", equalTo(true)))
                .andExpect(jsonPath("$.peopleLimit", equalTo(10)));
    }

    @Test
    void shouldSaveWorkoutClassToDatabase() throws Exception {
        String name = "dto save test";
        int limit = 5;
        String description = "description";
        WorkoutClassDto workoutClassDto = new WorkoutClassDto(
                null,
                name,
                description,
                true,
                limit,
                new HashSet<>(),
                null,
                new HashSet<>());
        assertFalse(classRepo.existsByName(name));

        mockMvc.perform(post("/api/workout/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutClassDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.description", equalTo(description)))
                .andExpect(jsonPath("$.peopleLimit", equalTo(limit)));
        assertTrue(classRepo.existsByName(name));
    }

    @Test
    void shouldGetWorkoutByName() throws Exception {
        String name = "workout123";
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity(
                name,
                true,
                15);
        assertFalse(classRepo.existsByName(name));
        classRepo.save(workoutClassEntity);

        mockMvc.perform(get("/api/workout/by_name?name={name}", name))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.available", equalTo(true)))
                .andExpect(jsonPath("$.peopleLimit", equalTo(15)));
    }

    @Test
    @Transactional
    void shouldMakeWorkoutUnavailableWhenDeleteById() throws Exception {
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("123name", true, 15);
        classRepo.save(workoutClassEntity);
        long id = workoutClassEntity.getId();

        mockMvc.perform(post("/api/workout/delete/" + id))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.name", equalTo("123name")))
                .andExpect(jsonPath("$.available", equalTo(false)));
        assertFalse(classRepo.getById(id).isAvailable());
    }

    @Test
    void shouldGetListOfAllAvailableWorkouts() throws Exception {
        WorkoutClassEntity workoutClassEntity1 = new WorkoutClassEntity("workout1", true, 15);
        WorkoutClassEntity workoutClassEntity2 = new WorkoutClassEntity("workout11", true, 15);
        WorkoutClassEntity workoutClassEntity3 = new WorkoutClassEntity("workout111", false, 15);
        classRepo.save(workoutClassEntity1);
        classRepo.save(workoutClassEntity2);
        classRepo.save(workoutClassEntity3);

        mockMvc.perform(get("/api/workout/available"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, true)));
    }

    @Test
    void shouldGetListOfAllWorkouts() throws Exception {
        WorkoutClassEntity workoutClassEntity1 = new WorkoutClassEntity("workout1", true, 15);
        WorkoutClassEntity workoutClassEntity2 = new WorkoutClassEntity("workout11", true, 15);
        WorkoutClassEntity workoutClassEntity3 = new WorkoutClassEntity("workout111", false, 15);
        classRepo.save(workoutClassEntity1);
        classRepo.save(workoutClassEntity2);
        classRepo.save(workoutClassEntity3);

        mockMvc.perform(get("/api/workout/all"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, true, false)));
    }

    @Test
    void shouldUpdateWorkoutById() throws Exception {
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("workout1", true, 15);
        classRepo.save(workoutClassEntity);
        long id = workoutClassEntity.getId();
        WorkoutClassDto workoutClassDto = new WorkoutClassDto(
                null,
                "Dancing Queen",
                "Description",
                false,
                25,
                new HashSet<>(),
                null,
                new HashSet<>());

        mockMvc.perform((patch("/api/workout/update?id={id}", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutClassDto)))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.name", equalTo("Dancing Queen")))
                .andExpect(jsonPath("$.description", equalTo("Description")))
                .andExpect(jsonPath("$.available", equalTo(false)))
                .andExpect(jsonPath("$.peopleLimit", equalTo(25)))
                .andExpect(jsonPath("$.clients", notNullValue()))
                .andExpect(jsonPath("$.sessions", notNullValue()));
    }

    @Test
    void shouldDeleteClientFromWorkoutById() throws Exception {
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("Super workout", true, 12);
        ClientEntity clientEntity = new ClientEntity(
                "A",
                "B",
                "ccc",
                LocalDate.of(1997, 6, 7),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        workoutService.addClientToClass(clientEntity, workoutClassEntity);
        classRepo.save(workoutClassEntity);
        long workoutId = workoutClassEntity.getId();
        long clientId = clientEntity.getId();

        mockMvc.perform((patch("/api/workout/delete_client?workoutId={workoutId}&clientId={clientId}",
                        workoutId, clientId)))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", equalTo("A")))
                .andExpect(jsonPath("$.lastName", equalTo("B")))
                .andExpect(jsonPath("$.passport", equalTo("ccc")));
    }

    @Test
    void shouldDeleteInstructorFromWorkoutById() throws Exception {
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("Super workout", true, 12);
        InstructorEntity instructorEntity = new InstructorEntity("A", "B", "ccc",
                LocalDate.of(1997, 6, 7), true);
        workoutService.addInstructorToClass(instructorEntity, workoutClassEntity);
        classRepo.save(workoutClassEntity);
        long workoutId = workoutClassEntity.getId();
        long instructorId = instructorEntity.getId();

        mockMvc.perform((patch("/api/workout/delete_instructor?workoutId={workoutId}&instructorId={instructorId}",
                        workoutId, instructorId)))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", equalTo("A")))
                .andExpect(jsonPath("$.lastName", equalTo("B")))
                .andExpect(jsonPath("$.passport", equalTo("ccc")));
    }

    @Test
    void shouldGetAllActiveClientsFromWorkoutByWorkoutName() throws Exception {
        String name = "Jumping lower";
        ClientEntity clientEntity1 = new ClientEntity(
                "Jim",
                "Bean",
                "15",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        ClientEntity clientEntity2 = new ClientEntity(
                "Bill",
                "Bean",
                "154",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        ClientEntity clientEntity3 = new ClientEntity(
                "Will",
                "Bean",
                "156",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, false),
                new HashSet<>());
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity(name, true, 5);
        workoutService.addClientToClass(clientEntity1, workoutClassEntity);
        workoutService.addClientToClass(clientEntity2, workoutClassEntity);
        workoutClassEntity.getClients().add(clientEntity3);
        clientEntity3.getClientWorkouts().add(workoutClassEntity);

        classRepo.save(workoutClassEntity);

        mockMvc.perform(get("/api/workout/active_clients?name={name}", name))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[*].membership.active", containsInAnyOrder(true, true)));
    }

    @Test
    @Transactional
    void shouldGetAllClassSessionsById() throws Exception {
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("Super workout", true, 12);
        WorkoutSessionEntity workoutSession1 = new WorkoutSessionEntity(
                null,
                Duration.ofMinutes(30),
                LocalDate.of(2022, 4, 4),
                LocalTime.of(12, 0),
                null);
        WorkoutSessionEntity workoutSession2 = new WorkoutSessionEntity(
                null,
                Duration.ofMinutes(40),
                LocalDate.of(2022, 3, 4),
                LocalTime.of(12, 0),
                null);
        WorkoutSessionEntity workoutSession3 = new WorkoutSessionEntity(
                null,
                Duration.ofMinutes(50),
                LocalDate.of(2022, 5, 4),
                LocalTime.of(12, 0),
                null);
        workoutClassEntity.getSessions().add(workoutSession1);
        workoutClassEntity.getSessions().add(workoutSession2);
        workoutClassEntity.getSessions().add(workoutSession3);
        classRepo.save(workoutClassEntity);
        long id = workoutClassEntity.getId();

        mockMvc.perform(get("/api/workout/sessions/" + id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[*].durationInMinutes", containsInAnyOrder(30 * 60d, 40 * 60d, 50 * 60d)));
    }

    @Test
    @Transactional
    void shouldDeleteClassSessionsByIds() throws Exception {
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("Super workout", true, 12);
        WorkoutSessionEntity workoutSession1 = new WorkoutSessionEntity(
                null,
                Duration.ofMinutes(30),
                LocalDate.of(2022, 4, 4),
                LocalTime.of(12, 0),
                null);
        WorkoutSessionEntity workoutSession2 = new WorkoutSessionEntity(
                null,
                Duration.ofMinutes(40),
                LocalDate.of(2022, 3, 4),
                LocalTime.of(12, 0),
                null);
        WorkoutSessionEntity workoutSession3 = new WorkoutSessionEntity(
                null,
                Duration.ofMinutes(50),
                LocalDate.of(2022, 5, 4),
                LocalTime.of(12, 0),
                null);
        workoutClassEntity.getSessions().add(workoutSession1);
        workoutClassEntity.getSessions().add(workoutSession2);
        workoutClassEntity.getSessions().add(workoutSession3);
        classRepo.save(workoutClassEntity);
        long classId = workoutClassEntity.getId();
        long sessionId = workoutSession2.getId();

        mockMvc.perform((delete("/api/workout/session/delete?classId={classId}&sessionId={sessionId}",
                        classId, sessionId)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.durationInMinutes", equalTo(40 * 60d)));

        assertFalse(classRepo.getById(classId).getSessions().contains(workoutSession2));
    }

    @Test
    @Transactional
    void shouldAddSessionToWorkoutClassById() throws Exception {
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("Super workout", true, 12);
        WorkoutSessionDto sessionDto = new WorkoutSessionDto(
                null,
                Duration.ofMinutes(30),
                LocalDate.of(2022, 4, 4),
                LocalTime.of(12, 0),
                null);
        classRepo.save(workoutClassEntity);
        long classId = workoutClassEntity.getId();


        mockMvc.perform(patch("/api/workout/session/add/" + classId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.durationInMinutes", equalTo(30 * 60d)));

        assertTrue(classRepo.getById(classId).getSessions().contains(converter.convertSessionDto(sessionDto)));
    }
}