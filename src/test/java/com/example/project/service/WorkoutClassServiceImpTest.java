package com.example.project.service;

import com.example.project.converter.Converter;
import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutClassDto;
import com.example.project.dto.WorkoutSessionDto;
import com.example.project.entity.*;
import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.example.project.repo.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;

import static com.example.project.exception.ExceptionMessageUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class WorkoutClassServiceImpTest {

    @Autowired
    Converter converter;

    @Autowired
    WorkoutClassRepo workoutClassRepo;

    @Autowired
    WorkoutClassServiceImp workoutService;

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    InstructorRepo instructorRepo;

    @Autowired
    WorkoutSessionRepo workoutSessionRepo;

    @Autowired
    MembershipRepo membershipRepo;

    @BeforeEach
    void setUp() {
        workoutClassRepo.deleteAll();
        clientRepo.deleteAll();
        membershipRepo.deleteAll();
        instructorRepo.deleteAll();
        workoutSessionRepo.deleteAll();
    }

    @Test
    void shouldAddInstructorToWorkout() {
        InstructorEntity instructorEntity =
                new InstructorEntity("Jim", "Bean", "15",
                        LocalDate.of(2000, 1, 1), true);
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Jumping higher", true, 5);
        assertNull(workoutClassEntity.getInstructor());

        workoutService.addInstructorToClass(instructorEntity, workoutClassEntity);

        assertEquals(workoutClassEntity.getInstructor(), instructorEntity);
    }

    @Test
    void shouldAddSessionToWorkoutClass() {
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Jumping higher", true, 5);
        WorkoutSessionEntity workoutSession = new WorkoutSessionEntity(null,
                Duration.ofMinutes(45),
                LocalDate.of(2022, 2, 2),
                LocalTime.of(16, 30),
                null);
        assertFalse(workoutClassEntity.getSessions().contains(workoutSession));

        workoutService.addSessionToClass(workoutSession, workoutClassEntity);

        assertTrue(workoutClassEntity.getSessions().contains(workoutSession));
    }

    @Test
    void shouldThrowCustomExceptionWhenAddingInstructorInSecondTime() {
        InstructorEntity instructorEntity =
                new InstructorEntity("Jim", "Bean", "15",
                        LocalDate.of(2000, 1, 1), true);
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Jumping higher", true, 5);
        workoutService.addInstructorToClass(instructorEntity, workoutClassEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addInstructorToClass(instructorEntity, workoutClassEntity));

        assertEquals("Instructor " + instructorEntity.getFirstName() + " already signed for this workout",
                exception.getMessage());
    }

    @Test
    void shouldAddClientToWorkout() {
        ClientEntity clientEntity = new ClientEntity(
                "Jim",
                "Bean",
                "15",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Jumping higher", true, 5);
        assertFalse(workoutClassEntity.getClients().contains(clientEntity));

        workoutService.addClientToClass(clientEntity, workoutClassEntity);

        assertTrue(workoutClassEntity.getClients().contains(clientEntity));
    }

    @Test
    void shouldGetAllSessionsFromWorkoutClass() {
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Jumping higher", true, 5);
        WorkoutSessionEntity workoutSession1 = new WorkoutSessionEntity(null,
                Duration.ofMinutes(45),
                LocalDate.of(2022, 2, 2),
                LocalTime.of(16, 30),
                null);
        WorkoutSessionEntity workoutSession2 = new WorkoutSessionEntity(null,
                Duration.ofMinutes(30),
                LocalDate.of(2022, 3, 3),
                LocalTime.of(16, 30),
                null);
        WorkoutSessionEntity workoutSession3 = new WorkoutSessionEntity(null,
                Duration.ofMinutes(60),
                LocalDate.of(2022, 1, 1),
                LocalTime.of(16, 30),
                null);
        workoutService.addSessionToClass(workoutSession1, workoutClassEntity);
        workoutService.addSessionToClass(workoutSession2, workoutClassEntity);
        workoutService.addSessionToClass(workoutSession3, workoutClassEntity);
        workoutClassRepo.save(workoutClassEntity);
        long id = workoutClassEntity.getId();

        List<WorkoutSessionDto> allClassSessions = workoutService.getAllClassSessions(id);

        assertEquals(3, allClassSessions.size());
        assertTrue(allClassSessions.stream()
                .anyMatch(workoutSessionDto ->
                        workoutSessionDto.getDurationInMinutes().equals(workoutSession3.getDurationInMinutes())));
    }

    @Test
    void shouldThrowCustomExceptionWhenGetAllSessionsFromWorkoutClassWithWrongId() {
        long id = -100L;
        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.getAllClassSessions(id));

        assertEquals(CLASS_NOT_FOUND_ID + id, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldDeleteSessionFromClassByIds(){
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Jumping higher", true, 5);
        WorkoutSessionEntity workoutSession = new WorkoutSessionEntity(null,
                Duration.ofMinutes(45),
                LocalDate.of(2022, 2, 2),
                LocalTime.of(16, 30),
                null);
        workoutService.addSessionToClass(workoutSession, workoutClassEntity);
        workoutClassRepo.save(workoutClassEntity);
        long classId = workoutClassEntity.getId();
        long sessionId = workoutSession.getId();
        WorkoutClassEntity saved = workoutClassRepo.getById(classId);
        assertTrue(saved.getSessions().contains(workoutSession));

        workoutService.deleteSession(classId, sessionId);

        assertFalse(saved.getSessions().contains(workoutSession));
    }

    @Test
    void shouldGetActiveClientsFromWorkoutByName() {
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
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Jumping lower", true, 5);
        workoutService.addClientToClass(clientEntity1, workoutClassEntity);
        workoutService.addClientToClass(clientEntity2, workoutClassEntity);
        workoutClassEntity.getClients().add(clientEntity3);
        clientEntity3.getClientWorkouts().add(workoutClassEntity);
        workoutClassRepo.save(workoutClassEntity);

        List<ClientDto> activeClients = workoutService.getActiveClientsByClassName("Jumping lower");

        assertTrue(activeClients.stream().allMatch(clientDto -> clientDto.getMembership().isActive()));
        assertEquals(2, activeClients.size());
        assertEquals(workoutService.getByName("Jumping lower").getClients().size(), workoutClassEntity.getClients().size());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddClientInSecondTime() {
        ClientEntity clientEntity = new ClientEntity(
                "Jim",
                "Bean",
                "15",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Jumping higher", true, 5);
        workoutService.addClientToClass(clientEntity, workoutClassEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addClientToClass(clientEntity, workoutClassEntity));

        assertEquals("Client " + clientEntity.getFirstName() + " already signed for this workout",
                exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddingClientOverTheLimitToWorkout() {
        ClientEntity clientFirst = new ClientEntity("White",
                "Horse",
                "145125",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Jumping higher", true, 1);
        workoutService.addClientToClass(clientFirst, workoutClassEntity);
        ClientEntity clientSecond = new ClientEntity("Jim",
                "Bean",
                "15",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addClientToClass(clientSecond, workoutClassEntity));

        assertEquals("All free slots has been taken for this workout", exception.getMessage());
    }

    @Test
    void shouldGetActiveClientsCounter() {
        ClientEntity clientFirst = new ClientEntity("White",
                "Horse",
                "145125",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        ClientEntity clientSecond = new ClientEntity("Jim",
                "Bean",
                "15",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, false),
                new HashSet<>());
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Jumping higher", true, 11);
        workoutService.addClientToClass(clientFirst, workoutClassEntity);
        workoutClassEntity.getClients().add(clientSecond);

        assertEquals(1, workoutService.showActiveClientsCounter(workoutClassEntity));
        assertTrue(workoutClassEntity.getClients().stream().
                filter(clientEntity -> clientEntity.getMembership().isActive()).
                allMatch(clientEntity -> clientEntity.equals(clientFirst)));
    }

    @Test
    @Transactional
    void getById() {
        WorkoutClassEntity workout = new WorkoutClassEntity("testOne", true, 100);
        WorkoutSessionEntity workoutSession = new WorkoutSessionEntity(null,
                Duration.ofMinutes(45),
                LocalDate.of(2022, 2, 2),
                LocalTime.of(16, 30),
                null);
        ClientEntity clientEntity1 = new ClientEntity(
                "one",
                "one",
                "101010",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        ClientEntity clientEntity2 = new ClientEntity(
                "one22",
                "one22",
                "10122010",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        InstructorEntity instructorEntity = new InstructorEntity("one1", "one1", "00001",
                LocalDate.of(2001, 2, 2), true);
        workoutService.addSessionToClass(workoutSession, workout);
        workoutService.addClientToClass(clientEntity1, workout);
        workoutService.addClientToClass(clientEntity2, workout);
        workoutService.addInstructorToClass(instructorEntity, workout);
        workoutClassRepo.save(workout);
        long id = workout.getId();

        WorkoutClassDto workoutClassDto = workoutService.getById(id);

        assertEquals(workout.getName(), workoutClassDto.getName());
        assertEquals(2, workoutClassDto.getClients().size());
        assertEquals(instructorEntity.getPassport(), workoutClassDto.getInstructor().getPassport());
        assertEquals(workout.getClients().size(), workoutClassDto.getClients().size());
        assertTrue(workoutClassDto.getClients().stream().
                anyMatch(clientDto -> clientDto.getPassport().equals(clientEntity1.getPassport())));
        assertEquals(workout.getPeopleLimit(), workoutClassDto.getPeopleLimit());
        assertTrue(workoutClassDto.getSessions().stream()
                .anyMatch(workoutSessionDto -> workoutSessionDto.getStartDate().equals(workoutSession.getStartDate())));
    }

    @Test
    void shouldThrowCustomExceptionWhenGetById() {
        long workoutId = -11;
        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.getById(workoutId));
        assertEquals(CLASS_NOT_FOUND_ID + workoutId, exception.getMessage());
    }

    @Test
    void shouldSaveWorkoutToDatabase() {
        String name = "TestWorkout";
        int peopleLimit = 10;
        InstructorDto instructorDto = new InstructorDto(
                null,
                "name",
                "lastname",
                "p2aqwsqw123",
                "address",
                "8888888",
                "speciality",
                "education",
                true,
                LocalDate.of(2000, 1, 1),
                new HashSet<>());
        WorkoutClassDto workoutClassDto = new WorkoutClassDto(
                null,
                name,
                "description",
                true,
                peopleLimit,
                new HashSet<>(),
                instructorDto,
                new HashSet<>());
        assertFalse(workoutClassRepo.existsByName(name));

        workoutService.save(workoutClassDto);

        assertTrue(workoutClassRepo.existsByName(name));
    }

    @Test
    void shouldThrowCustomExceptionWhenTryingToSaveAlreadyExistingWorkout() {
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("some workout", true, 10);
        workoutClassRepo.save(workoutClassEntity);
        WorkoutClassDto workoutClassDto = converter.convertWorkoutClassEntity(workoutClassEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.save(workoutClassDto));

        assertEquals(CLASS_ALREADY_EXISTS_NAME + workoutClassEntity.getName(), exception.getMessage());
    }

    @Test
    void shouldGetWorkoutByName() {
        String name = "YOGA BY SOMEONE";
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity(name, true, 100);
        workoutClassRepo.save(workoutClassEntity);

        WorkoutClassDto workoutClassDto = workoutService.getByName(name);

        assertEquals(workoutClassEntity.getName(), workoutClassDto.getName());
        assertEquals(workoutClassEntity.getPeopleLimit(), workoutClassDto.getPeopleLimit());
    }

    @Test
    void shouldThrowCustomExceptionWhenGetByName() {
        String name = "YOGA BY SOMEONE";

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.getByName(name));

        assertEquals(CLASS_NOT_FOUND_NAME + name, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldChangeAvailableInWorkoutWhenDeleteById() {
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("name", true, 100);
        workoutClassRepo.save(workoutClassEntity);
        Long id = workoutClassEntity.getId();

        workoutService.deleteById(id);

        assertFalse(workoutClassRepo.getById(id).isAvailable());
    }

    @Test
    @Transactional
    void shouldGetAllAvailableWorkoutsAndAllWorkouts() {
        WorkoutClassEntity workoutClassEntity1 = new WorkoutClassEntity();
        WorkoutClassEntity workoutClassEntity2 = new WorkoutClassEntity();
        WorkoutClassEntity workoutClassEntity3 = new WorkoutClassEntity();
        workoutClassEntity1.setAvailable(true);
        workoutClassEntity2.setAvailable(true);
        workoutClassEntity3.setAvailable(false);
        WorkoutClassEntity saved1 = workoutClassRepo.save(workoutClassEntity1);
        WorkoutClassEntity saved2 = workoutClassRepo.save(workoutClassEntity2);
        WorkoutClassEntity saved3 = workoutClassRepo.save(workoutClassEntity3);

        assertTrue(workoutService.getAll().contains(converter.convertWorkoutClassEntity(saved1)));
        assertTrue(workoutService.getAll().contains(converter.convertWorkoutClassEntity(saved2)));
        assertTrue(workoutService.getAll().contains(converter.convertWorkoutClassEntity(saved3)));

        assertTrue(workoutService.getAllAvailable().contains(converter.convertWorkoutClassEntity(saved1)));
        assertTrue(workoutService.getAllAvailable().contains(converter.convertWorkoutClassEntity(saved2)));
        assertFalse(workoutService.getAllAvailable().contains(converter.convertWorkoutClassEntity(saved3)));
    }

    @Test
    @Transactional
    void shouldAddClientToWorkoutByWorkoutNameAndClientId() {
        String workoutName = "TestName";
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity(workoutName, true, 10);
        workoutClassRepo.save(workoutClassEntity);
        long workoutId = workoutClassEntity.getId();
        ClientEntity clientEntity = new ClientEntity(
                "Poly",
                "Gaz",
                "d;;lkj",
                LocalDate.of(1995, 2, 14),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        assertEquals(0, workoutClassEntity.getClients().size());
        clientRepo.save(clientEntity);
        long clientId = clientEntity.getId();

        workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, clientId);

        WorkoutClassEntity savedWorkout = workoutClassRepo.getById(workoutId);
        assertTrue(savedWorkout.getClients().contains(clientEntity));
        assertEquals(1, savedWorkout.getClients().size());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddClientToWorkoutWithWrongClientId() {
        String workoutName = "TestName";
        long id = -123;

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, id));

        assertEquals(CLIENT_NOT_FOUND_ID + id, exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddClientToWorkoutWithWrongWorkoutName() {
        String workoutName = "TestName";
        ClientEntity clientEntity = new ClientEntity(
                "Bob",
                "Nebob",
                "asdfg",
                LocalDate.of(1995, 2, 14),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        clientRepo.save(clientEntity);
        long id = clientEntity.getId();

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, id));

        assertEquals(CLASS_NOT_FOUND_NAME + workoutName, exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddClientToWorkoutWithWrongId() {
        String workoutName = "TestName";
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity(workoutName, true, 10);
        workoutClassRepo.save(workoutClassEntity);
        long id = -123;

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, id));

        assertEquals(CLIENT_NOT_FOUND_ID + id, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldAddInstructorToWorkoutByWorkoutNameAndInstructorId() {
        String workoutName = "TestName22";
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity(workoutName, true, 10);
        workoutClassRepo.save(workoutClassEntity);
        long workoutId = workoutClassEntity.getId();
        InstructorEntity instructorEntity =
                new InstructorEntity("Igor", "Komarov", "ddeqqw",
                        LocalDate.of(1955, 1, 1), true);
        instructorRepo.save(instructorEntity);
        long instructorId = instructorEntity.getId();

        workoutService.addInstructorToWorkoutByWorkoutNameAndInstructorId(workoutName, instructorId);

        WorkoutClassEntity savedWorkout = workoutClassRepo.getById(workoutId);

        assertEquals(savedWorkout.getInstructor(), instructorEntity);
    }

    @Test
    void shouldThrowCustomExceptionWhenAddInstructorToWorkoutWithWrongName() {
        String workoutName = "TestName";
        long id = -123;

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addInstructorToWorkoutByWorkoutNameAndInstructorId(workoutName, id));

        assertEquals(CLASS_NOT_FOUND_NAME + workoutName, exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddInstructorToWorkoutWithWrongId() {
        String workoutName = "TestName";
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity(workoutName, true, 10);
        workoutClassRepo.save(workoutClassEntity);
        long id = -123;

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addInstructorToWorkoutByWorkoutNameAndInstructorId(workoutName, id));

        assertEquals(INSTRUCTOR_NOT_FOUND_ID + id, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldUpdateWorkoutById() throws JsonMappingException {
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Super workout", true, 12);
        ClientEntity clientEntity = new ClientEntity(
                "A",
                "B",
                "ccc",
                LocalDate.of(1997, 6, 7),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        InstructorEntity instructorEntity = new InstructorEntity(
                "Z",
                "X",
                "qqq",
                LocalDate.of(2000, 1, 1),
                true);
        WorkoutSessionEntity workoutSession = new WorkoutSessionEntity(null,
                Duration.ofMinutes(45),
                LocalDate.of(2022, 2, 2),
                LocalTime.of(16, 30),
                null);
        workoutService.addClientToClass(clientEntity, workoutClassEntity);
        workoutService.addInstructorToClass(instructorEntity, workoutClassEntity);
        workoutService.addSessionToClass(workoutSession, workoutClassEntity);
        workoutClassRepo.save(workoutClassEntity);
        Long id = workoutClassEntity.getId();
        String name = "Regular workout";
        String description = "some kind of description";
        boolean available = false;
        Integer limit = 222;
        WorkoutClassDto workoutClassDto = new WorkoutClassDto(null, name, description, available, limit, null, null, null);

        workoutService.updateById(id, workoutClassDto);
        WorkoutClassEntity checkWorkout = workoutClassRepo.getById(id);

        assertEquals(name, checkWorkout.getName());
        assertEquals(description, checkWorkout.getDescription());
        assertEquals(available, checkWorkout.isAvailable());
        assertEquals(limit, checkWorkout.getPeopleLimit());
        assertTrue(checkWorkout.getClients().contains(clientEntity));
        assertEquals(checkWorkout.getInstructor(), instructorEntity);
        assertTrue(workoutClassEntity.getSessions().contains(workoutSession));
    }

    @Test
    @Transactional
    void shouldThrowCustomExceptionWhenUpdateWorkoutByIdWithWrongId() {
        String name = "Regular workout";
        String description = "some kind of description";
        boolean available = false;
        Integer limit = 222;
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity(name, true, 12);
        workoutClassRepo.save(workoutClassEntity);
        Long id = workoutClassEntity.getId();
        WorkoutClassDto workoutClassDto = new WorkoutClassDto(
                null,
                name,
                description,
                available,
                limit,
                null,
                new InstructorDto(),
                null);

        CustomException exception = assertThrows(CustomException.class, () -> workoutService.updateById(id, workoutClassDto));

        assertEquals(CLASS_ALREADY_EXISTS_NAME + name, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldThrowCustomExceptionWhenUpdateWorkoutByIdWithWrongName() {
        Long id = -1000L;
        String name = "Regular workout";
        String description = "some kind of description";
        boolean available = false;
        Integer limit = 222;
        WorkoutClassDto workoutClassDto =
                new WorkoutClassDto(null, name, description, available, limit, null, new InstructorDto(), null);

        CustomException exception = assertThrows(CustomException.class, () -> workoutService.updateById(id, workoutClassDto));

        assertEquals(CLASS_NOT_FOUND_ID + id, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldDeleteClientFromWorkoutByIds() {
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Super workout", true, 12);
        ClientEntity clientEntity = new ClientEntity(
                "A",
                "B",
                "ccc",
                LocalDate.of(1997, 6, 7),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        workoutService.addClientToClass(clientEntity, workoutClassEntity);
        workoutClassRepo.save(workoutClassEntity);
        long workoutId = workoutClassEntity.getId();
        long clientId = clientEntity.getId();

        workoutService.deleteClientFromWorkoutByWorkoutIdAndClientId(workoutId, clientId);

        assertFalse(workoutClassRepo.getById(workoutId).getClients().contains(clientEntity));
        assertFalse(clientRepo.getById(clientId).getClientWorkouts().contains(workoutClassEntity));
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenDeleteClientFromWorkoutByIdsWithWrongWorkoutId() {
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Super workout", true, 12);
        ClientEntity clientEntity = new ClientEntity(
                "A",
                "B",
                "ccc",
                LocalDate.of(1997, 6, 7),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        workoutService.addClientToClass(clientEntity, workoutClassEntity);
        workoutClassRepo.save(workoutClassEntity);
        long workoutId = -1250L;
        long clientId = clientEntity.getId();

        CustomException exception = assertThrows(CustomException.class, () -> workoutService.deleteClientFromWorkoutByWorkoutIdAndClientId(workoutId, clientId));

        assertEquals(CLASS_NOT_FOUND_ID + workoutId, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenDeleteClientFromWorkoutByIdsWithWrongClientId() {
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Super workout", true, 12);
        ClientEntity clientEntity = new ClientEntity(
                "A",
                "B",
                "ccc",
                LocalDate.of(1997, 6, 7),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        workoutService.addClientToClass(clientEntity, workoutClassEntity);
        workoutClassRepo.save(workoutClassEntity);
        long workoutId = workoutClassEntity.getId();
        long clientId = -540L;

        CustomException exception = assertThrows(CustomException.class, () -> workoutService.deleteClientFromWorkoutByWorkoutIdAndClientId(workoutId, clientId));

        assertEquals(CLIENT_NOT_FOUND_ID + clientId, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldDeleteInstructorFromWorkoutByIds() {
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Super workout", true, 12);
        InstructorEntity instructorEntity =
                new InstructorEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
        workoutService.addInstructorToClass(instructorEntity, workoutClassEntity);
        workoutClassRepo.save(workoutClassEntity);
        long workoutId = workoutClassEntity.getId();
        long instructorId = instructorEntity.getId();

        workoutService.deleteInstructorFromWorkoutByWorkoutId(workoutId);

        assertNull(workoutClassRepo.getById(workoutId).getInstructor());
        assertFalse(instructorRepo.getById(instructorId).getInstructorWorkouts().contains(workoutClassEntity));
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenDeleteInstructorFromWorkoutByIdsWithWrongWorkoutId() {
        WorkoutClassEntity workoutClassEntity =
                new WorkoutClassEntity("Super workout", true, 12);
        InstructorEntity instructorEntity = new InstructorEntity(
                "A",
                "B",
                "ccc",
                LocalDate.of(1997, 6, 7),
                true);
        workoutService.addInstructorToClass(instructorEntity, workoutClassEntity);
        workoutClassRepo.save(workoutClassEntity);
        long workoutId = -1250L;

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.deleteInstructorFromWorkoutByWorkoutId(workoutId));

        assertEquals(CLASS_NOT_FOUND_ID + workoutId, exception.getMessage());
    }
}