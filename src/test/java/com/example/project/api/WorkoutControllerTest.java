package com.example.project.api;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "username", authorities = {"ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER"})
@RequiredArgsConstructor
class WorkoutControllerTest {
//
//    @Autowired
//    ClassRepo classRepo;
//
//    @Autowired
//    ClientRepo clientRepo;
//
//    @Autowired
//    InstructorRepo instructorRepo;
//
//    @Autowired
//    WorkoutWorkoutClassServiceImp workoutService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @BeforeEach
//    public void setup() {
//        classRepo.deleteAll();
//        clientRepo.deleteAll();
//        instructorRepo.deleteAll();
//    }
//
//    @Test
//    void shouldGetWorkoutById() throws Exception {
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("some sport", Duration.ofMinutes(90), true, 10);
//        classRepo.save(workoutClassEntity);
//        long id = workoutClassEntity.getId();
//
//        mockMvc.perform(get("/api/workout/by_id/" + id))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.name", equalTo("some sport")))
//                .andExpect(jsonPath("$.durationInMinutes", equalTo(60 * 90d)))
//                .andExpect(jsonPath("$.peopleLimit", equalTo(10)));
//    }
//
//    @Test
//    void shouldSaveClientToDatabase() throws Exception {
//        String name = "dto save test";
//        Duration duration = Duration.ofMinutes(15);
//        int limit = 5;
//        WorkoutClassDto workoutClassDto =
//                new WorkoutClassDto(null, name, duration, true, limit, new HashSet<>(), new HashSet<>());
//        assertFalse(classRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, duration, limit));
//
//        mockMvc.perform(post("/api/workout/save")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(workoutClassDto)))
//                .andExpect(status().isCreated())
//                .andDo(print())
//                .andExpect(jsonPath("$.name", equalTo(name)))
//                .andExpect(jsonPath("$.durationInMinutes", equalTo(15 * 60d)))
//                .andExpect(jsonPath("$.peopleLimit", equalTo(limit)));
//        assertTrue(classRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, duration, limit));
//    }
//
//    @Test
//    void shouldGetWorkoutByName() throws Exception {
//        String name = "workout123";
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity(name, Duration.ofMinutes(90), true, 15);
//        assertFalse(classRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, Duration.ofMinutes(30), 15));
//        classRepo.save(workoutClassEntity);
//
//        mockMvc.perform(get("/api/workout/by_name?name={name}", name))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.name", equalTo(name)))
//                .andExpect(jsonPath("$.durationInMinutes", equalTo(90 * 60d)))
//                .andExpect(jsonPath("$.peopleLimit", equalTo(15)));
//    }
//
//    @Test
//    @Transactional
//    void shouldMakeWorkoutUnavailableWhenDeleteById() throws Exception {
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("123name", Duration.ofMinutes(90), true, 15);
//        classRepo.save(workoutClassEntity);
//        long id = workoutClassEntity.getId();
//
//        mockMvc.perform(post("/api/workout/delete/" + id))
//                .andExpect(status().isAccepted())
//                .andDo(print())
//                .andExpect(jsonPath("$.name", equalTo("123name")))
//                .andExpect(jsonPath("$.available", equalTo(false)));
//        assertFalse(classRepo.getById(id).isAvailable());
//    }
//
//    @Test
//    void shouldGetListOfAllAvailableWorkouts() throws Exception {
//        WorkoutClassEntity workoutClassEntity1 =
//                new WorkoutClassEntity("workout1", Duration.ofMinutes(90), true, 15);
//        WorkoutClassEntity workoutClassEntity2 =
//                new WorkoutClassEntity("workout11", Duration.ofMinutes(60), true, 15);
//        WorkoutClassEntity workoutClassEntity3 =
//                new WorkoutClassEntity("workout111", Duration.ofMinutes(40), false, 15);
//        classRepo.save(workoutClassEntity1);
//        classRepo.save(workoutClassEntity2);
//        classRepo.save(workoutClassEntity3);
//
//        mockMvc.perform(get("/api/workout/available"))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$[*]", hasSize(2)))
//                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, true)));
//    }
//
//    @Test
//    void shouldGetListOfAllWorkouts() throws Exception {
//        WorkoutClassEntity workoutClassEntity1 =
//                new WorkoutClassEntity("workout1", Duration.ofMinutes(40), true, 15);
//        WorkoutClassEntity workoutClassEntity2 =
//                new WorkoutClassEntity("workout11", Duration.ofMinutes(60), true, 15);
//        WorkoutClassEntity workoutClassEntity3 =
//                new WorkoutClassEntity("workout111", Duration.ofMinutes(90), false, 15);
//        classRepo.save(workoutClassEntity1);
//        classRepo.save(workoutClassEntity2);
//        classRepo.save(workoutClassEntity3);
//
//        mockMvc.perform(get("/api/workout/all"))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$[*]", hasSize(3)))
//                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, true, false)));
//    }
//
//    @Test
//    void shouldUpdateWorkoutById() throws Exception {
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("workout1", Duration.ofMinutes(40), true, 15);
//        classRepo.save(workoutClassEntity);
//        long id = workoutClassEntity.getId();
//        WorkoutClassDto workoutClassDto = new WorkoutClassDto(null, "Dancing Queen", Duration.ofMinutes(35), false, 25, null, null);
//
//        mockMvc.perform((patch("/api/workout/update?id={id}", id))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(workoutClassDto)))
//                .andExpect(status().isAccepted())
//                .andDo(print())
//                .andExpect(jsonPath("$.name", equalTo("Dancing Queen")))
//                .andExpect(jsonPath("$.durationInMinutes", equalTo(35 * 60d)))
//                .andExpect(jsonPath("$.available", equalTo(false)))
//                .andExpect(jsonPath("$.peopleLimit", equalTo(25)))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//    }
//
//    @Test
//    void shouldDeleteClientFromWorkoutById() throws Exception {
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Super workout", Duration.ofMinutes(30), true, 12);
//        ClientEntity clientEntity =
//                new ClientEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
//        workoutService.addClientToWorkout(clientEntity, workoutClassEntity);
//        classRepo.save(workoutClassEntity);
//        long workoutId = workoutClassEntity.getId();
//        long clientId = clientEntity.getId();
//
//        mockMvc.perform((patch("/api/workout/delete_client?workoutId={workoutId}&clientId={clientId}", workoutId, clientId)))
//                .andExpect(status().isAccepted())
//                .andDo(print())
//                .andExpect(jsonPath("$.firstName", equalTo("A")))
//                .andExpect(jsonPath("$.lastName", equalTo("B")))
//                .andExpect(jsonPath("$.passport", equalTo("ccc")));
//    }
//
//    @Test
//    void shouldDeleteInstructorFromWorkoutById() throws Exception {
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Super workout", Duration.ofMinutes(30), true, 12);
//        InstructorEntity instructorEntity =
//                new InstructorEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
//        workoutService.addInstructorToWorkout(instructorEntity, workoutClassEntity);
//        classRepo.save(workoutClassEntity);
//        long workoutId = workoutClassEntity.getId();
//        long instructorId = instructorEntity.getId();
//
//        mockMvc.perform((patch("/api/workout/delete_instructor?workoutId={workoutId}&instructorId={instructorId}", workoutId, instructorId)))
//                .andExpect(status().isAccepted())
//                .andDo(print())
//                .andExpect(jsonPath("$.firstName", equalTo("A")))
//                .andExpect(jsonPath("$.lastName", equalTo("B")))
//                .andExpect(jsonPath("$.passport", equalTo("ccc")));
//    }
//
//    @Test
//    void shouldGetAllActiveClientsFromWorkoutByWorkoutName() throws Exception {
//        String name = "Jumping lower";
//        ClientEntity clientEntity1 =
//                new ClientEntity("Jim", "Bean", "15",
//                        LocalDate.of(2000, 1, 1), true);
//        ClientEntity clientEntity2 =
//                new ClientEntity("Bill", "Bean", "154",
//                        LocalDate.of(2000, 1, 1), true);
//        ClientEntity clientEntity3 =
//                new ClientEntity("Will", "Bean", "156",
//                        LocalDate.of(2000, 1, 1), false);
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity(name, Duration.ofMinutes(45), true, 5);
//        workoutService.addClientToWorkout(clientEntity1, workoutClassEntity);
//        workoutService.addClientToWorkout(clientEntity2, workoutClassEntity);
//        workoutClassEntity.getClients().add(clientEntity3);
//        clientEntity3.getClientWorkouts().add(workoutClassEntity);
//
//        classRepo.save(workoutClassEntity);
//
//        mockMvc.perform(get("/api/workout/active_clients?name={name}", name))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$[*]", hasSize(2)))
//                .andExpect(jsonPath("$[*].active", containsInAnyOrder(true, true)));
//    }
//
//    @Test
//    void shouldGetActiveInstructorsFromWorkoutByName() throws Exception {
//        String name = "Jumping lower";
//        InstructorEntity instructorEntity1 =
//                new InstructorEntity("one1", "one1", "00001", LocalDate.of(2001, 2, 2), true);
//        InstructorEntity instructorEntity2 =
//                new InstructorEntity("one11", "one11", "100001", LocalDate.of(2001, 2, 2), true);
//        InstructorEntity instructorEntity3 =
//                new InstructorEntity("one111", "one111", "1000011", LocalDate.of(2001, 2, 2), false);
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity(name, Duration.ofMinutes(45), true, 5);
//        workoutService.addInstructorToWorkout(instructorEntity1, workoutClassEntity);
//        workoutService.addInstructorToWorkout(instructorEntity2, workoutClassEntity);
//        workoutClassEntity.getInstructors().add(instructorEntity3);
//        instructorEntity3.getInstructorWorkouts().add(workoutClassEntity);
//
//        classRepo.save(workoutClassEntity);
//
//        mockMvc.perform(get("/api/workout/active_instructors?name={name}", name))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$[*]", hasSize(2)))
//                .andExpect(jsonPath("$[*].active", containsInAnyOrder(true, true)));
//    }
}