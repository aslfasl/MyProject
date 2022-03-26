package com.example.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor
class WorkoutClassServiceImpTest {
//    private final Duration durationTest = Duration.ofMinutes(45);
//
//    @Autowired
//    Converter converter;
//
//    @Autowired
//    ClassRepo classRepo;
//
//    @Autowired
//    WorkoutWorkoutClassServiceImp workoutService;
//
//    @Autowired
//    ClientRepo clientRepo;
//
//    @Autowired
//    InstructorRepo instructorRepo;
//
//    @BeforeEach
//    void setUp() {
//        classRepo.deleteAll();
//        clientRepo.deleteAll();
//        instructorRepo.deleteAll();
//    }
//
//    @Test
//    void shouldAddInstructorToWorkout() {
//        InstructorEntity instructorEntity =
//                new InstructorEntity("Jim", "Bean", "15",
//                        LocalDate.of(2000, 1, 1), true);
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Jumping higher", durationTest, true, 5);
//        assertFalse(workoutClassEntity.getInstructors().contains(instructorEntity));
//
//        workoutService.addInstructorToWorkout(instructorEntity, workoutClassEntity);
//
//        assertTrue(workoutClassEntity.getInstructors().contains(instructorEntity));
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenAddingInstructorInSecondTime() {
//        InstructorEntity instructorEntity =
//                new InstructorEntity("Jim", "Bean", "15",
//                        LocalDate.of(2000, 1, 1), true);
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Jumping higher", durationTest, true, 5);
//        workoutService.addInstructorToWorkout(instructorEntity, workoutClassEntity);
//
//        CustomException exception = assertThrows(CustomException.class,
//                () -> workoutService.addInstructorToWorkout(instructorEntity, workoutClassEntity));
//
//        assertEquals("Instructor " + instructorEntity.getFirstName() + " already signed for this workout",
//                exception.getMessage());
//    }
//
//    @Test
//    void shouldAddClientToWorkout() {
//        ClientEntity clientEntity =
//                new ClientEntity("Jim", "Bean", "15",
//                        LocalDate.of(2000, 1, 1), true);
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Jumping higher", durationTest, true, 5);
//        assertFalse(workoutClassEntity.getClients().contains(clientEntity));
//
//        workoutService.addClientToWorkout(clientEntity, workoutClassEntity);
//
//        assertTrue(workoutClassEntity.getClients().contains(clientEntity));
//    }
//
//    @Test
//    void shouldGetActiveClientsFromWorkoutByName() {
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
//                new WorkoutClassEntity("Jumping lower", durationTest, true, 5);
//        workoutService.addClientToWorkout(clientEntity1, workoutClassEntity);
//        workoutService.addClientToWorkout(clientEntity2, workoutClassEntity);
//        workoutClassEntity.getClients().add(clientEntity3);
//        clientEntity3.getClientWorkouts().add(workoutClassEntity);
//        classRepo.save(workoutClassEntity);
//
//        List<ClientDto> activeClients = workoutService.getActiveClientsByWorkoutName("Jumping lower");
//
//        assertTrue(activeClients.stream().allMatch(ClientDto::isActive));
//        assertEquals(2, activeClients.size());
//        assertEquals(workoutService.getByName("Jumping lower").getClients().size(), workoutClassEntity.getClients().size());
//    }
//
//    @Test
//    void shouldGetActiveInstructorsFromWorkoutByName() {
//        InstructorEntity instructorEntity1 =
//                new InstructorEntity("one1", "one1", "00001", LocalDate.of(2001, 2, 2), true);
//        InstructorEntity instructorEntity2 =
//                new InstructorEntity("one11", "one11", "100001", LocalDate.of(2001, 2, 2), true);
//        InstructorEntity instructorEntity3 =
//                new InstructorEntity("one111", "one111", "1000011", LocalDate.of(2001, 2, 2), false);
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Jumping lower", durationTest, true, 5);
//        workoutService.addInstructorToWorkout(instructorEntity1, workoutClassEntity);
//        workoutService.addInstructorToWorkout(instructorEntity2, workoutClassEntity);
//        workoutClassEntity.getInstructors().add(instructorEntity3);
//        instructorEntity3.getInstructorWorkouts().add(workoutClassEntity);
//        classRepo.save(workoutClassEntity);
//
//        List<InstructorDto> activeInstructors = workoutService.getActiveInstructorsByWorkoutName("Jumping lower");
//
//        assertTrue(activeInstructors.stream().allMatch(InstructorDto::isActive));
//        assertEquals(2, activeInstructors.size());
//        assertEquals(workoutService.getByName("Jumping lower").getClients().size(), workoutClassEntity.getClients().size());
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenAddClientInSecondTime() {
//        ClientEntity clientEntity =
//                new ClientEntity("Jim", "Bean", "15",
//                        LocalDate.of(2000, 1, 1), true);
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Jumping higher", durationTest, true, 5);
//        workoutService.addClientToWorkout(clientEntity, workoutClassEntity);
//
//        CustomException exception = assertThrows(CustomException.class,
//                () -> workoutService.addClientToWorkout(clientEntity, workoutClassEntity));
//
//        assertEquals("Client " + clientEntity.getFirstName() + " already signed for this workout",
//                exception.getMessage());
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenAddingClientOverTheLimitToWorkout() {
//        ClientEntity clientFirst =
//                new ClientEntity("White", "Horse", "145125",
//                        LocalDate.of(2000, 1, 1), true);
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Jumping higher", durationTest, true, 1);
//        workoutService.addClientToWorkout(clientFirst, workoutClassEntity);
//
//        ClientEntity clientSecond =
//                new ClientEntity("Jim", "Bean", "15",
//                        LocalDate.of(2000, 1, 1), true);
//        CustomException exception = assertThrows(CustomException.class,
//                () -> workoutService.addClientToWorkout(clientSecond, workoutClassEntity));
//
//        assertEquals("All free slots has been taken for this workout", exception.getMessage());
//    }
//
//    @Test
//    void shouldGetActiveClientsCounter() {
//        ClientEntity clientFirst =
//                new ClientEntity("White", "Horse", "145125",
//                        LocalDate.of(2000, 1, 1), true);
//        ClientEntity clientSecond =
//                new ClientEntity("Jim", "Bean", "15",
//                        LocalDate.of(2000, 1, 1), false);
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Jumping higher", durationTest, true, 11);
//        workoutService.addClientToWorkout(clientFirst, workoutClassEntity);
//        workoutClassEntity.getClients().add(clientSecond);
//
//        assertEquals(1, workoutService.showActiveClientsCounter(workoutClassEntity));
//        assertTrue(workoutClassEntity.getClients().stream().
//                filter(BaseEntity::isActive).
//                allMatch(clientEntity -> clientEntity.equals(clientFirst)));
//    }
//
//    @Test
//    @Transactional
//    void getById() {
//        WorkoutClassEntity workout = new WorkoutClassEntity("testOne", durationTest, true, 100);
//        ClientEntity clientEntity1 =
//                new ClientEntity("one", "one", "101010", LocalDate.of(2000, 1, 1), true);
//        ClientEntity clientEntity2 =
//                new ClientEntity("one22", "one22", "10122010", LocalDate.of(2000, 1, 1), true);
//        InstructorEntity instructorEntity1 =
//                new InstructorEntity("one1", "one1", "00001", LocalDate.of(2001, 2, 2), true);
//        InstructorEntity instructorEntity2 =
//                new InstructorEntity("one11", "one11", "100001", LocalDate.of(2001, 2, 2), true);
//        workoutService.addClientToWorkout(clientEntity1, workout);
//        workoutService.addClientToWorkout(clientEntity2, workout);
//        workoutService.addInstructorToWorkout(instructorEntity1, workout);
//        workoutService.addInstructorToWorkout(instructorEntity2, workout);
//        classRepo.save(workout);
//        long id = workout.getId();
//
//        WorkoutClassDto workoutClassDto = workoutService.getById(id);
//
//        assertEquals(workout.getName(), workoutClassDto.getName());
//        assertEquals(2, workoutClassDto.getClients().size());
//        assertEquals(2, workoutClassDto.getInstructors().size());
//        assertEquals(workout.getClients().size(), workoutClassDto.getClients().size());
//        assertEquals(workout.getDurationInMinutes(), workoutClassDto.getDurationInMinutes());
//        assertEquals(workout.getPeopleLimit(), workoutClassDto.getPeopleLimit());
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenGetById() {
//        long workoutId = -11;
//        CustomException exception = assertThrows(CustomException.class,
//                () -> workoutService.getById(workoutId));
//        assertEquals(CLASS_NOT_FOUND_ID + workoutId, exception.getMessage());
//    }
//
//    @Test
//    void shouldSaveWorkoutToDatabase() {
//        String name = "TestWorkout";
//        Duration durationInMin = durationTest;
//        int peopleLimit = 10;
//        WorkoutClassDto workoutClassDto =
//                new WorkoutClassDto(null, name, durationInMin, true, peopleLimit, new HashSet<>(), new HashSet<>());
//        assertFalse(classRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, durationInMin, peopleLimit));
//
//        workoutService.save(workoutClassDto);
//
//        assertTrue(classRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, durationInMin, peopleLimit));
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenTryingToSaveAlreadyExistingWorkout() {
//        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("some workout", durationTest, true, 10);
//        classRepo.save(workoutClassEntity);
//        WorkoutClassDto workoutClassDto = converter.convertWorkoutEntity(workoutClassEntity);
//
//        CustomException exception = assertThrows(CustomException.class,
//                () -> workoutService.save(workoutClassDto));
//
//        assertEquals(CLASS_ALREADY_EXISTS_NAME + workoutClassEntity.getName(), exception.getMessage());
//    }
//
//    @Test
//    void shouldGetWorkoutByName() {
//        String name = "YOGA BY SOMEONE";
//        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity(name, durationTest, true, 100);
//        classRepo.save(workoutClassEntity);
//
//        WorkoutClassDto workoutClassDto = workoutService.getByName(name);
//
//        assertEquals(workoutClassEntity.getName(), workoutClassDto.getName());
//        assertEquals(workoutClassEntity.getDurationInMinutes(), workoutClassDto.getDurationInMinutes());
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenGetByName() {
//        String name = "YOGA BY SOMEONE";
//
//        CustomException exception = assertThrows(CustomException.class,
//                () -> workoutService.getByName(name));
//
//        assertEquals(CLASS_NOT_FOUND_NAME + name, exception.getMessage());
//    }
//
//    @Test
//    @Transactional
//    void shouldChangeAvailableInWorkoutWhenDeleteById() {
//        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("name", durationTest, true, 100);
//        classRepo.save(workoutClassEntity);
//        Long id = workoutClassEntity.getId();
//
//        workoutService.deleteById(id);
//
//        assertFalse(classRepo.getById(id).isAvailable());
//    }
//
//    @Test
//    @Transactional
//    void shouldGetAllAvailableWorkoutsAndAllWorkouts() {
//        WorkoutClassEntity workoutClassEntity1 = new WorkoutClassEntity();
//        WorkoutClassEntity workoutClassEntity2 = new WorkoutClassEntity();
//        WorkoutClassEntity workoutClassEntity3 = new WorkoutClassEntity();
//        workoutClassEntity1.setAvailable(true);
//        workoutClassEntity2.setAvailable(true);
//        workoutClassEntity3.setAvailable(false);
//        WorkoutClassEntity saved1 = classRepo.save(workoutClassEntity1);
//        WorkoutClassEntity saved2 = classRepo.save(workoutClassEntity2);
//        WorkoutClassEntity saved3 = classRepo.save(workoutClassEntity3);
//
//        assertTrue(workoutService.getAll().contains(converter.convertWorkoutEntity(saved1)));
//        assertTrue(workoutService.getAll().contains(converter.convertWorkoutEntity(saved2)));
//        assertTrue(workoutService.getAll().contains(converter.convertWorkoutEntity(saved3)));
//
//        assertTrue(workoutService.getAllAvailable().contains(converter.convertWorkoutEntity(saved1)));
//        assertTrue(workoutService.getAllAvailable().contains(converter.convertWorkoutEntity(saved2)));
//        assertFalse(workoutService.getAllAvailable().contains(converter.convertWorkoutEntity(saved3)));
//    }
//
//    @Test
//    @Transactional
//    void shouldAddClientToWorkoutByWorkoutNameAndClientId() {
//        String workoutName = "TestName";
//        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity(workoutName, durationTest, true, 10);
//        classRepo.save(workoutClassEntity);
//        long workoutId = workoutClassEntity.getId();
//        ClientEntity clientEntity =
//                new ClientEntity("Poly", "Gaz", "d;;lkj", LocalDate.of(1995, 2, 14), true);
//        assertEquals(0, workoutClassEntity.getClients().size());
//        clientRepo.save(clientEntity);
//        long clientId = clientEntity.getId();
//
//        workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, clientId);
//
//        WorkoutClassEntity savedWorkout = classRepo.getById(workoutId);
//        assertTrue(savedWorkout.getClients().contains(clientEntity));
//        assertEquals(1, savedWorkout.getClients().size());
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenAddClientToWorkoutWithWrongClientId() {
//        String workoutName = "TestName";
//        long id = -123;
//
//        CustomException exception = assertThrows(CustomException.class,
//                () -> workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, id));
//
//        assertEquals(CLIENT_NOT_FOUND_ID + id, exception.getMessage());
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenAddClientToWorkoutWithWrongWorkoutName() {
//        String workoutName = "TestName";
//        ClientEntity clientEntity =
//                new ClientEntity("Bob", "Nebob", "asdfg", LocalDate.of(1991, 5, 5), true);
//        clientRepo.save(clientEntity);
//        long id = clientEntity.getId();
//
//        CustomException exception = assertThrows(CustomException.class,
//                () -> workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, id));
//
//        assertEquals(CLASS_NOT_FOUND_NAME + workoutName, exception.getMessage());
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenAddClientToWorkoutWithWrongId() {
//        String workoutName = "TestName";
//        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity(workoutName, durationTest, true, 10);
//        classRepo.save(workoutClassEntity);
//        long id = -123;
//
//        CustomException exception = assertThrows(CustomException.class,
//                () -> workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, id));
//
//        assertEquals(CLIENT_NOT_FOUND_ID + id, exception.getMessage());
//    }
//
//
//    @Test
//    @Transactional
//    void shouldAddInstructorToWorkoutByWorkoutNameAndInstructorId() {
//        String workoutName = "TestName22";
//        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity(workoutName, durationTest, true, 10);
//        classRepo.save(workoutClassEntity);
//        long workoutId = workoutClassEntity.getId();
//        InstructorEntity instructorEntity =
//                new InstructorEntity("Igor", "Komarov", "ddeqqw",
//                        LocalDate.of(1955, 1, 1), true);
//        instructorRepo.save(instructorEntity);
//        long instructorId = instructorEntity.getId();
//
//        workoutService.addInstructorToWorkoutByWorkoutNameAndInstructorId(workoutName, instructorId);
//
//        WorkoutClassEntity savedWorkout = classRepo.getById(workoutId);
//
//        assertTrue(savedWorkout.getInstructors().contains(instructorEntity));
//        assertEquals(1, savedWorkout.getInstructors().size());
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenAddInstructorToWorkoutWithWrongName() {
//        String workoutName = "TestName";
//        long id = -123;
//
//        CustomException exception = assertThrows(CustomException.class,
//                () -> workoutService.addInstructorToWorkoutByWorkoutNameAndInstructorId(workoutName, id));
//
//        assertEquals(CLASS_NOT_FOUND_NAME + workoutName, exception.getMessage());
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenAddInstructorToWorkoutWithWrongId() {
//        String workoutName = "TestName";
//        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity(workoutName, durationTest, true, 10);
//        classRepo.save(workoutClassEntity);
//        long id = -123;
//
//        CustomException exception = assertThrows(CustomException.class,
//                () -> workoutService.addInstructorToWorkoutByWorkoutNameAndInstructorId(workoutName, id));
//
//        assertEquals(INSTRUCTOR_NOT_FOUND_ID + id, exception.getMessage());
//    }
//
//    @Test
//    @Transactional
//    void shouldUpdateWorkoutById() throws JsonMappingException {
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Super workout", durationTest, true, 12);
//        ClientEntity clientEntity =
//                new ClientEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
//        InstructorEntity instructorEntity =
//                new InstructorEntity("Z", "X", "qqq", LocalDate.of(2000, 1, 1), true);
//        workoutService.addClientToWorkout(clientEntity, workoutClassEntity);
//        workoutService.addInstructorToWorkout(instructorEntity, workoutClassEntity);
//        classRepo.save(workoutClassEntity);
//        Long id = workoutClassEntity.getId();
//        String name = "Regular workout";
//        Duration duration = null;
//        boolean available = false;
//        Integer limit = 222;
//        WorkoutClassDto workoutClassDto = new WorkoutClassDto(null, name, duration, available, limit, null, null);
//
//        workoutService.updateById(id, workoutClassDto);
//        WorkoutClassEntity checkWorkout = classRepo.getById(id);
//
//        assertEquals(name, checkWorkout.getName());
//        assertEquals(limit, checkWorkout.getPeopleLimit());
//        assertEquals(workoutClassEntity.getDurationInMinutes(), checkWorkout.getDurationInMinutes());
//        assertTrue(checkWorkout.getClients().contains(clientEntity));
//        assertTrue(checkWorkout.getInstructors().contains(instructorEntity));
//    }
//
//    @Test
//    @Transactional
//    void shouldThrowCustomExceptionWhenUpdateWorkoutByIdWithWrongId() {
//        String name = "Regular workout";
//        Duration duration = null;
//        boolean available = false;
//        Integer limit = 222;
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity(name, durationTest, true, 12);
//        classRepo.save(workoutClassEntity);
//        Long id = workoutClassEntity.getId();
//        WorkoutClassDto workoutClassDto = new WorkoutClassDto(null, name, duration, available, limit, null, null);
//
//        CustomException exception = assertThrows(CustomException.class, () -> workoutService.updateById(id, workoutClassDto));
//
//        assertEquals(CLASS_ALREADY_EXISTS_NAME + name, exception.getMessage());
//    }
//
//    @Test
//    @Transactional
//    void shouldThrowCustomExceptionWhenUpdateWorkoutByIdWithWrongName() {
//        Long id = -1000L;
//        String name = "Regular workout";
//        Duration duration = null;
//        boolean available = false;
//        Integer limit = 222;
//        WorkoutClassDto workoutClassDto = new WorkoutClassDto(null, name, duration, available, limit, null, null);
//
//        CustomException exception = assertThrows(CustomException.class, () -> workoutService.updateById(id, workoutClassDto));
//
//        assertEquals(CLASS_NOT_FOUND_ID + id, exception.getMessage());
//    }
//
//    @Test
//    @Transactional
//    void shouldDeleteClientFromWorkoutByIds(){
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Super workout", durationTest, true, 12);
//        ClientEntity clientEntity =
//                new ClientEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
//        workoutService.addClientToWorkout(clientEntity, workoutClassEntity);
//        classRepo.save(workoutClassEntity);
//        long workoutId = workoutClassEntity.getId();
//        long clientId = clientEntity.getId();
//
//        workoutService.deleteClientFromWorkoutByWorkoutIdAndClientId(workoutId, clientId);
//
//        assertFalse(classRepo.getById(workoutId).getClients().contains(clientEntity));
//        assertFalse(clientRepo.getById(clientId).getClientWorkouts().contains(workoutClassEntity));
//    }
//
//    @Test
//    @Transactional
//    void shouldThrowExceptionWhenDeleteClientFromWorkoutByIdsWithWrongWorkoutId(){
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Super workout", durationTest, true, 12);
//        ClientEntity clientEntity =
//                new ClientEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
//        workoutService.addClientToWorkout(clientEntity, workoutClassEntity);
//        classRepo.save(workoutClassEntity);
//        long workoutId = -1250L;
//        long clientId = clientEntity.getId();
//
//        CustomException exception = assertThrows(CustomException.class, () -> workoutService.deleteClientFromWorkoutByWorkoutIdAndClientId(workoutId, clientId));
//
//        assertEquals(CLASS_NOT_FOUND_ID + workoutId, exception.getMessage());
//    }
//
//    @Test
//    @Transactional
//    void shouldThrowExceptionWhenDeleteClientFromWorkoutByIdsWithWrongClientId(){
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Super workout", durationTest, true, 12);
//        ClientEntity clientEntity =
//                new ClientEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
//        workoutService.addClientToWorkout(clientEntity, workoutClassEntity);
//        classRepo.save(workoutClassEntity);
//        long workoutId = workoutClassEntity.getId();
//        long clientId = -540L;
//
//        CustomException exception = assertThrows(CustomException.class, () -> workoutService.deleteClientFromWorkoutByWorkoutIdAndClientId(workoutId, clientId));
//
//        assertEquals(CLIENT_NOT_FOUND_ID + clientId, exception.getMessage());
//    }
//
//    @Test
//    @Transactional
//    void shouldDeleteInstructorFromWorkoutByIds(){
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Super workout", durationTest, true, 12);
//        InstructorEntity instructorEntity =
//                new InstructorEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
//        workoutService.addInstructorToWorkout(instructorEntity, workoutClassEntity);
//        classRepo.save(workoutClassEntity);
//        long workoutId = workoutClassEntity.getId();
//        long instructorId = instructorEntity.getId();
//
//        workoutService.deleteInstructorFromWorkoutByWorkoutIdAndInstructorId(workoutId, instructorId);
//
//        assertFalse(classRepo.getById(workoutId).getInstructors().contains(instructorEntity));
//        assertFalse(instructorRepo.getById(instructorId).getInstructorWorkouts().contains(workoutClassEntity));
//    }
//
//    @Test
//    @Transactional
//    void shouldThrowExceptionWhenDeleteInstructorFromWorkoutByIdsWithWrongWorkoutId(){
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Super workout", durationTest, true, 12);
//        InstructorEntity instructorEntity =
//                new InstructorEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
//        workoutService.addInstructorToWorkout(instructorEntity, workoutClassEntity);
//        classRepo.save(workoutClassEntity);
//        long workoutId = -1250L;
//        long instructorId = instructorEntity.getId();
//
//        CustomException exception = assertThrows(CustomException.class, () -> workoutService.deleteInstructorFromWorkoutByWorkoutIdAndInstructorId(workoutId, instructorId));
//
//        assertEquals(CLASS_NOT_FOUND_ID + workoutId, exception.getMessage());
//    }
//
//    @Test
//    @Transactional
//    void shouldThrowExceptionWhenDeleteInstructorFromWorkoutByIdsWithWrongInstructorId(){
//        WorkoutClassEntity workoutClassEntity =
//                new WorkoutClassEntity("Super workout", durationTest, true, 12);
//        InstructorEntity instructorEntity =
//                new InstructorEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
//        workoutService.addInstructorToWorkout(instructorEntity, workoutClassEntity);
//        classRepo.save(workoutClassEntity);
//        long workoutId = workoutClassEntity.getId();
//        long instructorId = -540L;
//
//        CustomException exception = assertThrows(CustomException.class, () -> workoutService.deleteInstructorFromWorkoutByWorkoutIdAndInstructorId(workoutId, instructorId));
//
//        assertEquals(INSTRUCTOR_NOT_FOUND_ID + instructorId, exception.getMessage());
//    }
}