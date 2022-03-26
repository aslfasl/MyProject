package com.example.project.service;

import com.example.project.dto.*;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.MembershipEntity;
import com.example.project.entity.WorkoutClassEntity;
import com.example.project.exception.CustomException;
import com.example.project.repo.ClientRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.ExampleMatcher;

import java.time.LocalDate;
import java.util.*;

import static com.example.project.exception.ExceptionMessageUtils.CLIENT_ALREADY_EXISTS_PASSPORT;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@SpringBootTest
class ClientServiceImpTest {
    //    private final Duration durationTest = Duration.ofMinutes(45);
//
    @Autowired
    private ClientServiceImp service;

    @Autowired
    private ClientServiceImp clientService;

    @Autowired
    private ClientRepo clientRepo;

    ExampleMatcher modelMatcher = ExampleMatcher.matching()
            .withIgnorePaths("id")
            .withMatcher("passport", ignoreCase());

    @AfterEach
    void after() {
        clientRepo.deleteAll();
    }

    @Test
    void shouldAddWorkoutToClientEntity() {
        ClientEntity clientEntity = new ClientEntity("Jack",
                "Dogson",
                "890123",
                LocalDate.of(1989, 1, 1));
        MembershipEntity membership = new MembershipEntity(null,
                LocalDate.now(),
                LocalDate.now().plusMonths(6),
                true,
                null);
        clientEntity.setMembership(membership);

        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("circle running",
                true,
                100);
        assertFalse(clientEntity.getClientWorkouts().contains(workoutClassEntity));

        clientService.addWorkoutToClient(clientEntity, workoutClassEntity);

        assertEquals(1, clientEntity.getClientWorkouts().size());
        assertTrue(clientEntity.getClientWorkouts().contains(workoutClassEntity));
    }

    @Test
    void shouldThrowCustomExceptionWhenAddWorkoutInSecondTime() {
        ClientEntity clientEntity =
                new ClientEntity("Jack",
                        "Dogson",
                        "890123",
                        LocalDate.of(1989, 1, 1));
        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("circle running",
                true,
                100);
        clientService.addWorkoutToClient(clientEntity, workoutClassEntity);

        CustomException exception = assertThrows(CustomException.class,
                () -> clientService.addWorkoutToClient(clientEntity, workoutClassEntity));

        assertEquals("This client already signed for: " + workoutClassEntity.getName(), exception.getMessage());
    }

    @Test
    void shouldSaveClient() {
        MembershipDto membershipDto = new MembershipDto(
                LocalDate.now(),
                LocalDate.now().plusMonths(6),
                true);
        ClientDto clientDto = new ClientDto(null,
                "Clint",
                "Eastwood",
                "123",
                "address",
                "123456",
                LocalDate.of(2000, 1, 1),
                membershipDto,
                new HashSet<>());
        clientDto.getClientWorkouts().add(new WorkoutClassDto());
        assertFalse(clientRepo.existsByPassport("123"));

        ClientDto client = service.saveClient(clientDto);
        assertTrue(clientRepo.existsByPassport("123"));
        assertEquals(clientDto.getBirthdate(), client.getBirthdate());
        assertEquals(clientDto.getClientWorkouts().size(), client.getClientWorkouts().size());
        assertEquals(clientDto.getMembership().getEndDate(), client.getMembership().getEndDate());
    }

    @Test
    void shouldSaveClientWithMembership() {
        MembershipDto membershipDto = new MembershipDto(
                LocalDate.now(),
                LocalDate.now().plusMonths(6),
                true);
        ClientDto clientDto = new ClientDto(null,
                "Clint",
                "Eastwood",
                "123",
                "address",
                "123456",
                LocalDate.of(2000, 1, 1),
                null,
                new HashSet<>());
        assertFalse(clientRepo.existsByPassport("123"));

        clientService.saveClientWithMembership(clientDto, membershipDto);
        ClientEntity savedClient = clientRepo.findByPassport("123");

        assertTrue(clientRepo.existsByPassport("123"));
        assertEquals(clientDto.getBirthdate(), savedClient.getBirthdate());
        assertEquals(clientDto.getMembership().getEndDate(), savedClient.getMembership().getEndDate());
    }

    @Test
    void shouldThrowCustomExceptionWhenSaveClientAlreadyExists() {
        ClientDto clientDto = new ClientDto(null,
                "Clint",
                "Eastwood",
                "123",
                "address",
                "123456",
                LocalDate.of(2000, 1, 1),
                new MembershipDto(null, null, true),
                new HashSet<>());
        ClientEntity clientEntity = new ClientEntity("Clint",
                "Eastwood",
                "123",
                LocalDate.of(2000, 1, 1));
        clientRepo.save(clientEntity);

        CustomException exception = assertThrows(CustomException.class, () -> service.saveClient(clientDto));

        assertEquals(CLIENT_ALREADY_EXISTS_PASSPORT + clientDto.getPassport(), exception.getMessage());
    }

//    @Test
//    void shouldGetClientById() {
//        ClientEntity clientEntity = new ClientEntity("NameFirst",
//                "SurnameFirst",
//                "414141",
//                LocalDate.of(2000, 1, 1),
//                true);
//        assertFalse(clientRepo.exists(Example.of(clientEntity, modelMatcher)));
//        clientRepo.save(clientEntity);
//        assertTrue(clientRepo.exists(Example.of(clientEntity, modelMatcher)));
//        long id = clientEntity.getId();
//
//        ClientDto client = service.getClientById(id);
//
//        assertEquals(clientEntity.getPassport(), client.getPassport());
//        assertEquals(client.getBirthdate(), client.getBirthdate());
//    }
//
//    @Test
//    @Transactional
//    void shouldChangeClientIsActiveToFalseWhenDeleteById() {
//        ClientEntity clientEntity = new ClientEntity("NameFirst",
//                "SurnameFirst",
//                "414141",
//                LocalDate.of(2000, 1, 1),
//                true);
//        clientRepo.save(clientEntity);
//        long id = clientEntity.getId();
//
//        ClientDto clientDto = service.deleteClientById(id);
//
//        assertFalse(clientDto.isActive());
//        assertFalse(clientRepo.getById(id).isActive());
//    }
//
//    @Test
//    @Transactional
//    void shouldUpdateClientEntityById() throws JsonMappingException {
//        ClientEntity clientEntity = new ClientEntity("NameFirst", "SurnameFirst", "414141",
//                LocalDate.of(2000, 1, 1), false);
//        WorkoutClassEntity workoutClassEntity = new WorkoutClassEntity("basketball", durationTest, true, 12);
//        clientService.addWorkoutToClient(clientEntity, workoutClassEntity);
//        clientRepo.save(clientEntity);
//        long id = clientEntity.getId();
//        String newFirstname = "Anna", newLastname = "Ivanova", newPassport = "fffda123",
//                newAddress = "address", newPhone = "123456";
//        LocalDate newBirthdate = LocalDate.of(1995, 5, 5);
//        boolean newActive = true;
//        ClientDto clientOverride = new ClientDto(null, newFirstname, newLastname, newPassport, newAddress,
//                newPhone, newBirthdate, newActive, null);
//
//
//        service.updateClientById(id, clientOverride);
//
//        ClientEntity clientSaved = clientRepo.getById(id);
//        assertEquals(newFirstname, clientSaved.getFirstName());
//        assertEquals(newLastname, clientSaved.getLastName());
//        assertEquals(newPassport, clientSaved.getPassport());
//        assertEquals(newBirthdate, clientSaved.getBirthdate());
//        assertEquals(newActive, clientSaved.isActive());
//        assertEquals(clientEntity.getClientWorkouts().size(), clientSaved.getClientWorkouts().size());
//        assertTrue(clientSaved.getClientWorkouts().contains(workoutClassEntity));
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenUpdateByIdWithAlreadyTakenPassport() {
//        String passport = "414141";
//        ClientEntity clientEntity = new ClientEntity("NameFirst", "SurnameFirst", passport,
//                LocalDate.of(2000, 1, 1), false);
//        clientRepo.save(clientEntity);
//        long id = clientEntity.getId();
//        ClientDto clientDto = new ClientDto(null, null, null, passport, null,
//                null, null, true, null);
//
//        CustomException exception = assertThrows(CustomException.class,
//                () -> service.updateClientById(id, clientDto));
//
//        assertEquals(CLIENT_ALREADY_EXISTS_PASSPORT + passport, exception.getMessage());
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenUpdateByIdNotFound() {
//        Long id = -22L;
//        ClientDto clientDto = new ClientDto();
//
//        CustomException exception = assertThrows(CustomException.class,
//                () -> service.updateClientById(id, clientDto));
//
//        assertEquals(CLIENT_NOT_FOUND_ID + id, exception.getMessage());
//    }
//
//    @Test
//    void shouldGetAllClientsByFullNameAndBirthDate() {
//        String name = "Bob";
//        String lastName = "Lee";
//        LocalDate localDate = LocalDate.of(2000, 1, 1);
//        ClientEntity clientEntity1 = new ClientEntity(name, lastName, "414141",
//                localDate, true);
//        ClientEntity clientEntity2 = new ClientEntity(name, lastName, "3333",
//                localDate, true);
//        ClientEntity clientEntity3 = new ClientEntity(name, lastName, "12111",
//                localDate, true);
//        clientRepo.save(clientEntity1);
//        clientRepo.save(clientEntity2);
//        clientRepo.save(clientEntity3);
//
//        List<ClientDto> clients = service.getClientByFullNameAndBirthDate(name, lastName, localDate);
//        assertTrue(clients.stream().allMatch(clientDto -> clientDto.getFirstName().equals(name)));
//        assertTrue(clients.stream().allMatch(clientDto -> clientDto.getLastName().equals(lastName)));
//        assertTrue(clients.stream().allMatch(clientDto -> clientDto.getBirthdate().equals(localDate)));
//        assertEquals(3, clients.size());
//    }
//
//    @Test
//    void shouldGetAllClientsAndAllActiveClients() {
//        ClientEntity clientEntity1 = new ClientEntity("Name1", "last1", "fdd", LocalDate.of(2000, 1, 1), true);
//        ClientEntity clientEntity2 = new ClientEntity("Name2", "last2", "ds", LocalDate.of(2000, 1, 1), true);
//        ClientEntity clientEntity3 = new ClientEntity("Name3", "last3", "asd", LocalDate.of(2000, 1, 1), false);
//        clientRepo.save(clientEntity1);
//        clientRepo.save(clientEntity2);
//        clientRepo.save(clientEntity3);
//
//        List<ClientDto> allActiveClients = service.getAllActiveClients();
//        assertEquals(3, service.getAll().size());
//        assertEquals(2, allActiveClients.size());
//        assertTrue(allActiveClients.stream().allMatch(ClientDto::isActive));
//    }
//
//    @Test
//    void shouldGetClientByPassport() {
//        String passport = "7777";
//        ClientEntity clientEntity = new ClientEntity("Name1", "last1", passport, LocalDate.of(2000, 1, 1), true);
//        clientRepo.save(clientEntity);
//
//        ClientDto clientDto = service.getClientByPassport(passport);
//
//        assertEquals(clientEntity.getPassport(), clientDto.getPassport());
//        assertEquals(clientEntity.getBirthdate(), clientDto.getBirthdate());
//        assertEquals(clientEntity.getFirstName(), clientDto.getFirstName());
//    }
//
//    @Test
//    void shouldThrowCustomExceptionWhenGetClientByPassportNotFound() {
//        String passport = "7777";
//
//        CustomException exception = assertThrows(CustomException.class, () -> service.getClientByPassport(passport));
//
//        assertEquals(CLIENT_NOT_FOUND_PASSPORT + passport, exception.getMessage());
//    }
//
//    @Test
//    void shouldGetFilteredAndSortedClients() {
//        ClientPage clientPage = new ClientPage();
//        clientPage.setPageNumber(0);
//        clientPage.setPageSize(5);
//        clientPage.setSortDirection(Sort.Direction.DESC);
//        clientPage.setSortBy("firstName");
//        ClientSearchCriteria clientSearchCriteria = new ClientSearchCriteria();
//        clientSearchCriteria.setFirstName("dre");
//        clientSearchCriteria.setLastName("lko");
//
//        ClientEntity clientEntity1 = new ClientEntity("Andrey", "Volkov", "passport", LocalDate.of(2000, 1, 1), true);
//        ClientEntity clientEntity2 = new ClientEntity("Odrei", "Bolkob", "passport1", LocalDate.of(2000, 1, 1), true);
//        ClientEntity clientEntity3 = new ClientEntity("Asdf", "Qwer", "passport2", LocalDate.of(2000, 1, 1), true);
//        clientRepo.save(clientEntity1);
//        clientRepo.save(clientEntity2);
//        clientRepo.save(clientEntity3);
//
//        Page<ClientDto> resultPage = clientService.getClientsFilterPage(clientPage, clientSearchCriteria);
//        assertEquals(2, resultPage.getTotalElements());
//        assertEquals("Odrei", resultPage.getContent().get(0).getFirstName());
//        assertEquals("Andrey", resultPage.getContent().get(1).getFirstName());
//    }
}