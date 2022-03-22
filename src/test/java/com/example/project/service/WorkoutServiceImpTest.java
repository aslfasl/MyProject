package com.example.project.service;

import com.example.project.converter.Converter;
import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.BaseEntity;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.InstructorEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.exception.CustomException;
import com.example.project.repo.ClientRepo;
import com.example.project.repo.InstructorRepo;
import com.example.project.repo.WorkoutRepo;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;


import static com.example.project.exception.ExceptionMessageUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class WorkoutServiceImpTest {
    private final Duration durationTest = Duration.ofMinutes(45);

    @Autowired
    Converter converter;

    @Autowired
    WorkoutRepo workoutRepo;

    @Autowired
    WorkoutServiceImp workoutService;

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
    void shouldAddInstructorToWorkout() {
        InstructorEntity instructorEntity =
                new InstructorEntity("Jim", "Bean", "15",
                        LocalDate.of(2000, 1, 1), true);
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Jumping higher", durationTest, true, 5);
        assertFalse(workoutEntity.getInstructors().contains(instructorEntity));

        workoutService.addInstructorToWorkout(instructorEntity, workoutEntity);

        assertTrue(workoutEntity.getInstructors().contains(instructorEntity));
    }

    @Test
    void shouldThrowCustomExceptionWhenAddingInstructorInSecondTime() {
        InstructorEntity instructorEntity =
                new InstructorEntity("Jim", "Bean", "15",
                        LocalDate.of(2000, 1, 1), true);
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Jumping higher", durationTest, true, 5);
        workoutService.addInstructorToWorkout(instructorEntity, workoutEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addInstructorToWorkout(instructorEntity, workoutEntity));

        assertEquals("Instructor " + instructorEntity.getFirstName() + " already signed for this workout",
                exception.getMessage());
    }

    @Test
    void shouldAddClientToWorkout() {
        ClientEntity clientEntity =
                new ClientEntity("Jim", "Bean", "15",
                        LocalDate.of(2000, 1, 1), true);
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Jumping higher", durationTest, true, 5);
        assertFalse(workoutEntity.getClients().contains(clientEntity));

        workoutService.addClientToWorkout(clientEntity, workoutEntity);

        assertTrue(workoutEntity.getClients().contains(clientEntity));
    }

    @Test
    void shouldGetActiveClientsFromWorkoutByName() {
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
                new WorkoutEntity("Jumping lower", durationTest, true, 5);
        workoutService.addClientToWorkout(clientEntity1, workoutEntity);
        workoutService.addClientToWorkout(clientEntity2, workoutEntity);
        workoutEntity.getClients().add(clientEntity3);
        clientEntity3.getClientWorkouts().add(workoutEntity);
        workoutRepo.save(workoutEntity);

        List<ClientDto> activeClients = workoutService.getActiveClientsByWorkoutName("Jumping lower");

        assertTrue(activeClients.stream().allMatch(ClientDto::isActive));
        assertEquals(2, activeClients.size());
        assertEquals(workoutService.getByName("Jumping lower").getClients().size(), workoutEntity.getClients().size());
    }

    @Test
    void shouldGetActiveInstructorsFromWorkoutByName() {
        InstructorEntity instructorEntity1 =
                new InstructorEntity("one1", "one1", "00001", LocalDate.of(2001, 2, 2), true);
        InstructorEntity instructorEntity2 =
                new InstructorEntity("one11", "one11", "100001", LocalDate.of(2001, 2, 2), true);
        InstructorEntity instructorEntity3 =
                new InstructorEntity("one111", "one111", "1000011", LocalDate.of(2001, 2, 2), false);
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Jumping lower", durationTest, true, 5);
        workoutService.addInstructorToWorkout(instructorEntity1, workoutEntity);
        workoutService.addInstructorToWorkout(instructorEntity2, workoutEntity);
        workoutEntity.getInstructors().add(instructorEntity3);
        instructorEntity3.getInstructorWorkouts().add(workoutEntity);
        workoutRepo.save(workoutEntity);

        List<InstructorDto> activeInstructors = workoutService.getActiveInstructorsByWorkoutName("Jumping lower");

        assertTrue(activeInstructors.stream().allMatch(InstructorDto::isActive));
        assertEquals(2, activeInstructors.size());
        assertEquals(workoutService.getByName("Jumping lower").getClients().size(), workoutEntity.getClients().size());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddClientInSecondTime() {
        ClientEntity clientEntity =
                new ClientEntity("Jim", "Bean", "15",
                        LocalDate.of(2000, 1, 1), true);
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Jumping higher", durationTest, true, 5);
        workoutService.addClientToWorkout(clientEntity, workoutEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addClientToWorkout(clientEntity, workoutEntity));

        assertEquals("Client " + clientEntity.getFirstName() + " already signed for this workout",
                exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddingClientOverTheLimitToWorkout() {
        ClientEntity clientFirst =
                new ClientEntity("White", "Horse", "145125",
                        LocalDate.of(2000, 1, 1), true);
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Jumping higher", durationTest, true, 1);
        workoutService.addClientToWorkout(clientFirst, workoutEntity);

        ClientEntity clientSecond =
                new ClientEntity("Jim", "Bean", "15",
                        LocalDate.of(2000, 1, 1), true);
        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addClientToWorkout(clientSecond, workoutEntity));

        assertEquals("All free slots has been taken for this workout", exception.getMessage());
    }

    @Test
    void shouldGetActiveClientsCounter() {
        ClientEntity clientFirst =
                new ClientEntity("White", "Horse", "145125",
                        LocalDate.of(2000, 1, 1), true);
        ClientEntity clientSecond =
                new ClientEntity("Jim", "Bean", "15",
                        LocalDate.of(2000, 1, 1), false);
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Jumping higher", durationTest, true, 11);
        workoutService.addClientToWorkout(clientFirst, workoutEntity);
        workoutEntity.getClients().add(clientSecond);

        assertEquals(1, workoutService.showActiveClientsCounter(workoutEntity));
        assertTrue(workoutEntity.getClients().stream().
                filter(BaseEntity::isActive).
                allMatch(clientEntity -> clientEntity.equals(clientFirst)));
    }

    @Test
    @Transactional
    void getById() {
        WorkoutEntity workout = new WorkoutEntity("testOne", durationTest, true, 100);
        ClientEntity clientEntity1 =
                new ClientEntity("one", "one", "101010", LocalDate.of(2000, 1, 1), true);
        ClientEntity clientEntity2 =
                new ClientEntity("one22", "one22", "10122010", LocalDate.of(2000, 1, 1), true);
        InstructorEntity instructorEntity1 =
                new InstructorEntity("one1", "one1", "00001", LocalDate.of(2001, 2, 2), true);
        InstructorEntity instructorEntity2 =
                new InstructorEntity("one11", "one11", "100001", LocalDate.of(2001, 2, 2), true);
        workoutService.addClientToWorkout(clientEntity1, workout);
        workoutService.addClientToWorkout(clientEntity2, workout);
        workoutService.addInstructorToWorkout(instructorEntity1, workout);
        workoutService.addInstructorToWorkout(instructorEntity2, workout);
        workoutRepo.save(workout);
        long id = workout.getId();

        WorkoutDto workoutDto = workoutService.getById(id);

        assertEquals(workout.getName(), workoutDto.getName());
        assertEquals(2, workoutDto.getClients().size());
        assertEquals(2, workoutDto.getInstructors().size());
        assertEquals(workout.getClients().size(), workoutDto.getClients().size());
        assertEquals(workout.getDurationInMinutes(), workoutDto.getDurationInMinutes());
        assertEquals(workout.getPeopleLimit(), workoutDto.getPeopleLimit());
    }

    @Test
    void shouldThrowCustomExceptionWhenGetById() {
        long workoutId = -11;
        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.getById(workoutId));
        assertEquals(WORKOUT_NOT_FOUND_ID + workoutId, exception.getMessage());
    }

    @Test
    void shouldSaveWorkoutToDatabase() {
        String name = "TestWorkout";
        Duration durationInMin = durationTest;
        int peopleLimit = 10;
        WorkoutDto workoutDto =
                new WorkoutDto(null, name, durationInMin, true, peopleLimit, new HashSet<>(), new HashSet<>());
        assertFalse(workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, durationInMin, peopleLimit));

        workoutService.save(workoutDto);

        assertTrue(workoutRepo.existsByNameAndDurationInMinutesAndPeopleLimit(name, durationInMin, peopleLimit));
    }

    @Test
    void shouldThrowCustomExceptionWhenTryingToSaveAlreadyExistingWorkout() {
        WorkoutEntity workoutEntity = new WorkoutEntity("some workout", durationTest, true, 10);
        workoutRepo.save(workoutEntity);
        WorkoutDto workoutDto = converter.convertWorkoutEntity(workoutEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.save(workoutDto));

        assertEquals(WORKOUT_ALREADY_EXISTS_NAME + workoutEntity.getName(), exception.getMessage());
    }

    @Test
    void shouldGetWorkoutByName() {
        String name = "YOGA BY SOMEONE";
        WorkoutEntity workoutEntity = new WorkoutEntity(name, durationTest, true, 100);
        workoutRepo.save(workoutEntity);

        WorkoutDto workoutDto = workoutService.getByName(name);

        assertEquals(workoutEntity.getName(), workoutDto.getName());
        assertEquals(workoutEntity.getDurationInMinutes(), workoutDto.getDurationInMinutes());
    }

    @Test
    void shouldThrowCustomExceptionWhenGetByName() {
        String name = "YOGA BY SOMEONE";

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.getByName(name));

        assertEquals(WORKOUT_NOT_FOUND_NAME + name, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldChangeAvailableInWorkoutWhenDeleteById() {
        WorkoutEntity workoutEntity = new WorkoutEntity("name", durationTest, true, 100);
        workoutRepo.save(workoutEntity);
        Long id = workoutEntity.getId();

        workoutService.deleteById(id);

        assertFalse(workoutRepo.getById(id).isAvailable());
    }

    @Test
    @Transactional
    void shouldGetAllAvailableWorkoutsAndAllWorkouts() {
        WorkoutEntity workoutEntity1 = new WorkoutEntity();
        WorkoutEntity workoutEntity2 = new WorkoutEntity();
        WorkoutEntity workoutEntity3 = new WorkoutEntity();
        workoutEntity1.setAvailable(true);
        workoutEntity2.setAvailable(true);
        workoutEntity3.setAvailable(false);
        WorkoutEntity saved1 = workoutRepo.save(workoutEntity1);
        WorkoutEntity saved2 = workoutRepo.save(workoutEntity2);
        WorkoutEntity saved3 = workoutRepo.save(workoutEntity3);

        //FIXED. workoutService.getAllAvailavle() and .getAll() returns DTOs
        // And I do not have an id in DTO's, so this is the only way i came up with.
// TODO: 16.03.2022 search by saved.getId() presence not by size
        assertTrue(workoutService.getAll().contains(converter.convertWorkoutEntity(saved1)));
        assertTrue(workoutService.getAll().contains(converter.convertWorkoutEntity(saved2)));
        assertTrue(workoutService.getAll().contains(converter.convertWorkoutEntity(saved3)));

        assertTrue(workoutService.getAllAvailable().contains(converter.convertWorkoutEntity(saved1)));
        assertTrue(workoutService.getAllAvailable().contains(converter.convertWorkoutEntity(saved2)));
        assertFalse(workoutService.getAllAvailable().contains(converter.convertWorkoutEntity(saved3)));
    }

    @Test
    @Transactional
    void shouldAddClientToWorkoutByWorkoutNameAndClientId() {
        String workoutName = "TestName";
        WorkoutEntity workoutEntity = new WorkoutEntity(workoutName, durationTest, true, 10);
        workoutRepo.save(workoutEntity);
        long workoutId = workoutEntity.getId();
        ClientEntity clientEntity =
                new ClientEntity("Poly", "Gaz", "d;;lkj", LocalDate.of(1995, 2, 14), true);
        assertEquals(0, workoutEntity.getClients().size());
        clientRepo.save(clientEntity);
        long clientId = clientEntity.getId();

        workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, clientId);

        WorkoutEntity savedWorkout = workoutRepo.getById(workoutId);
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
        ClientEntity clientEntity =
                new ClientEntity("Bob", "Nebob", "asdfg", LocalDate.of(1991, 5, 5), true);
        clientRepo.save(clientEntity);
        long id = clientEntity.getId();

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, id));

        assertEquals(WORKOUT_NOT_FOUND_NAME + workoutName, exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddClientToWorkoutWithWrongId() {
        String workoutName = "TestName";
        WorkoutEntity workoutEntity = new WorkoutEntity(workoutName, durationTest, true, 10);
        workoutRepo.save(workoutEntity);
        long id = -123;

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addClientToWorkoutByWorkoutNameAndClientId(workoutName, id));

        assertEquals(CLIENT_NOT_FOUND_ID + id, exception.getMessage());
    }


    @Test
    @Transactional
    void shouldAddInstructorToWorkoutByWorkoutNameAndInstructorId() {
        String workoutName = "TestName22";
        WorkoutEntity workoutEntity = new WorkoutEntity(workoutName, durationTest, true, 10);
        workoutRepo.save(workoutEntity);
        long workoutId = workoutEntity.getId();
        InstructorEntity instructorEntity =
                new InstructorEntity("Igor", "Komarov", "ddeqqw",
                        LocalDate.of(1955, 1, 1), true);
        instructorRepo.save(instructorEntity);
        long instructorId = instructorEntity.getId();

        workoutService.addInstructorToWorkoutByWorkoutNameAndInstructorId(workoutName, instructorId);

        WorkoutEntity savedWorkout = workoutRepo.getById(workoutId);
        // FIXED.
        // FIXME: 16.03.2022 fix test javax.persistence.EntityNotFoundException: Unable to find com.example.project.entity.WorkoutEntity with id 19
        assertTrue(savedWorkout.getInstructors().contains(instructorEntity));
        assertEquals(1, savedWorkout.getInstructors().size());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddInstructorToWorkoutWithWrongName() {
        String workoutName = "TestName";
        long id = -123;

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addInstructorToWorkoutByWorkoutNameAndInstructorId(workoutName, id));

        assertEquals(WORKOUT_NOT_FOUND_NAME + workoutName, exception.getMessage());
    }

    @Test
    void shouldThrowCustomExceptionWhenAddInstructorToWorkoutWithWrongId() {
        String workoutName = "TestName";
        WorkoutEntity workoutEntity = new WorkoutEntity(workoutName, durationTest, true, 10);
        workoutRepo.save(workoutEntity);
        long id = -123;

        CustomException exception = assertThrows(CustomException.class,
                () -> workoutService.addInstructorToWorkoutByWorkoutNameAndInstructorId(workoutName, id));

        assertEquals(INSTRUCTOR_NOT_FOUND_ID + id, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldUpdateWorkoutById() throws JsonMappingException {
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Super workout", durationTest, true, 12);
        ClientEntity clientEntity =
                new ClientEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
        InstructorEntity instructorEntity =
                new InstructorEntity("Z", "X", "qqq", LocalDate.of(2000, 1, 1), true);
        workoutService.addClientToWorkout(clientEntity, workoutEntity);
        workoutService.addInstructorToWorkout(instructorEntity, workoutEntity);
        workoutRepo.save(workoutEntity);
        Long id = workoutEntity.getId();
        String name = "Regular workout";
        Duration duration = null;
        boolean available = false;
        Integer limit = 222;
        WorkoutDto workoutDto = new WorkoutDto(null, name, duration, available, limit, null, null);

        workoutService.updateById(id, workoutDto);
        WorkoutEntity checkWorkout = workoutRepo.getById(id);

        assertEquals(name, checkWorkout.getName());
        assertEquals(limit, checkWorkout.getPeopleLimit());
        assertEquals(workoutEntity.getDurationInMinutes(), checkWorkout.getDurationInMinutes());
        assertTrue(checkWorkout.getClients().contains(clientEntity));
        assertTrue(checkWorkout.getInstructors().contains(instructorEntity));
    }

    @Test
    @Transactional
    void shouldThrowCustomExceptionWhenUpdateWorkoutByIdWithWrongId() {
        String name = "Regular workout";
        Duration duration = null;
        boolean available = false;
        Integer limit = 222;
        WorkoutEntity workoutEntity =
                new WorkoutEntity(name, durationTest, true, 12);
        workoutRepo.save(workoutEntity);
        Long id = workoutEntity.getId();
        WorkoutDto workoutDto = new WorkoutDto(null, name, duration, available, limit, null, null);

        CustomException exception = assertThrows(CustomException.class, () -> workoutService.updateById(id, workoutDto));

        assertEquals(WORKOUT_ALREADY_EXISTS_NAME + name, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldThrowCustomExceptionWhenUpdateWorkoutByIdWithWrongName() throws JsonMappingException {
        Long id = -1000L;
        String name = "Regular workout";
        Duration duration = null;
        boolean available = false;
        Integer limit = 222;
        WorkoutDto workoutDto = new WorkoutDto(null, name, duration, available, limit, null, null);

        CustomException exception = assertThrows(CustomException.class, () -> workoutService.updateById(id, workoutDto));

        assertEquals(WORKOUT_NOT_FOUND_ID + id, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldDeleteClientFromWorkoutByIds(){
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Super workout", durationTest, true, 12);
        ClientEntity clientEntity =
                new ClientEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
        workoutService.addClientToWorkout(clientEntity, workoutEntity);
        workoutRepo.save(workoutEntity);
        long workoutId = workoutEntity.getId();
        long clientId = clientEntity.getId();

        workoutService.deleteClientFromWorkoutByWorkoutIdAndClientId(workoutId, clientId);

        assertFalse(workoutRepo.getById(workoutId).getClients().contains(clientEntity));
        assertFalse(clientRepo.getById(clientId).getClientWorkouts().contains(workoutEntity));
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenDeleteClientFromWorkoutByIdsWithWrongWorkoutId(){
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Super workout", durationTest, true, 12);
        ClientEntity clientEntity =
                new ClientEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
        workoutService.addClientToWorkout(clientEntity, workoutEntity);
        workoutRepo.save(workoutEntity);
        long workoutId = -1250L;
        long clientId = clientEntity.getId();

        CustomException exception = assertThrows(CustomException.class, () -> workoutService.deleteClientFromWorkoutByWorkoutIdAndClientId(workoutId, clientId));

        assertEquals(WORKOUT_NOT_FOUND_ID + workoutId, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenDeleteClientFromWorkoutByIdsWithWrongClientId(){
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Super workout", durationTest, true, 12);
        ClientEntity clientEntity =
                new ClientEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
        workoutService.addClientToWorkout(clientEntity, workoutEntity);
        workoutRepo.save(workoutEntity);
        long workoutId = workoutEntity.getId();
        long clientId = -540L;

        CustomException exception = assertThrows(CustomException.class, () -> workoutService.deleteClientFromWorkoutByWorkoutIdAndClientId(workoutId, clientId));

        assertEquals(CLIENT_NOT_FOUND_ID + clientId, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldDeleteInstructorFromWorkoutByIds(){
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Super workout", durationTest, true, 12);
        InstructorEntity instructorEntity =
                new InstructorEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
        workoutService.addInstructorToWorkout(instructorEntity, workoutEntity);
        workoutRepo.save(workoutEntity);
        long workoutId = workoutEntity.getId();
        long instructorId = instructorEntity.getId();

        workoutService.deleteInstructorFromWorkoutByWorkoutIdAndInstructorId(workoutId, instructorId);

        assertFalse(workoutRepo.getById(workoutId).getInstructors().contains(instructorEntity));
        assertFalse(instructorRepo.getById(instructorId).getInstructorWorkouts().contains(workoutEntity));
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenDeleteInstructorFromWorkoutByIdsWithWrongWorkoutId(){
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Super workout", durationTest, true, 12);
        InstructorEntity instructorEntity =
                new InstructorEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
        workoutService.addInstructorToWorkout(instructorEntity, workoutEntity);
        workoutRepo.save(workoutEntity);
        long workoutId = -1250L;
        long instructorId = instructorEntity.getId();

        CustomException exception = assertThrows(CustomException.class, () -> workoutService.deleteInstructorFromWorkoutByWorkoutIdAndInstructorId(workoutId, instructorId));

        assertEquals(WORKOUT_NOT_FOUND_ID + workoutId, exception.getMessage());
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenDeleteInstructorFromWorkoutByIdsWithWrongInstructorId(){
        WorkoutEntity workoutEntity =
                new WorkoutEntity("Super workout", durationTest, true, 12);
        InstructorEntity instructorEntity =
                new InstructorEntity("A", "B", "ccc", LocalDate.of(1997, 6, 7), true);
        workoutService.addInstructorToWorkout(instructorEntity, workoutEntity);
        workoutRepo.save(workoutEntity);
        long workoutId = workoutEntity.getId();
        long instructorId = -540L;

        CustomException exception = assertThrows(CustomException.class, () -> workoutService.deleteInstructorFromWorkoutByWorkoutIdAndInstructorId(workoutId, instructorId));

        assertEquals(INSTRUCTOR_NOT_FOUND_ID + instructorId, exception.getMessage());
    }
}