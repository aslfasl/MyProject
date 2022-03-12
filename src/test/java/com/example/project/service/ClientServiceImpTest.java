package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.ClientEntity;
import com.example.project.repo.ClientRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@SpringBootTest
class ClientServiceImpTest {

    @Autowired
    private ClientServiceImp service;

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
    void shouldSaveClient() {
        ClientDto clientDto = new ClientDto("Clint", "Eastwood", "123",
                LocalDate.of(2000, 1, 1), true, new HashSet<>());
        clientDto.getClientWorkouts().add(new WorkoutDto());
        assertFalse(clientRepo.existsByPassport("123"));

        ClientDto client = service.saveClient(clientDto);
        assertTrue(clientRepo.existsByPassport("123"));
        assertEquals(clientDto.getBirthdate(), client.getBirthdate());
        assertEquals(clientDto.getClientWorkouts().size(), client.getClientWorkouts().size());
    }

    @Test
    void shouldGetClientById() {
        ClientEntity clientEntity = new ClientEntity("NameFirst",
                "SurnameFirst",
                "414141",
                LocalDate.of(2000, 1, 1),
                true);
        assertFalse(clientRepo.exists(Example.of(clientEntity, modelMatcher)));
        clientRepo.save(clientEntity);
        assertTrue(clientRepo.exists(Example.of(clientEntity, modelMatcher)));
        long id = clientEntity.getId();

        ClientDto client = service.getClientById(id);

        assertEquals(clientEntity.getPassport(), client.getPassport());
        assertEquals(client.getBirthdate(), client.getBirthdate());
    }

    @Test
    @Transactional
    void shouldChangeClientIsActiveToFalseWhenDeleteById() {
        ClientEntity clientEntity = new ClientEntity("NameFirst",
                "SurnameFirst",
                "414141",
                LocalDate.of(2000, 1, 1),
                true);
        clientRepo.save(clientEntity);
        long id = clientEntity.getId();

        ClientDto clientDto = service.deleteClientById(id);

        assertFalse(clientDto.isActive());
        assertFalse(clientRepo.getById(id).isActive());
    }

    @Test
    void shouldGetAllClientsByFullNameAndBirthDate() {
        String name = "Bob";
        String lastName = "Lee";
        LocalDate localDate = LocalDate.of(2000,1,1);
        ClientEntity clientEntity1 = new ClientEntity(name, lastName, "414141",
                localDate, true);
        ClientEntity clientEntity2 = new ClientEntity(name, lastName, "3333",
                localDate, true);
        ClientEntity clientEntity3 = new ClientEntity(name, lastName, "12111",
                localDate, true);
        clientRepo.save(clientEntity1);
        clientRepo.save(clientEntity2);
        clientRepo.save(clientEntity3);

        List<ClientDto> clients = service.getClientByFullNameAndBirthDate(name, lastName, localDate);
        assertTrue(clients.stream().allMatch(clientDto -> clientDto.getFirstName().equals(name)));
        assertTrue(clients.stream().allMatch(clientDto -> clientDto.getLastName().equals(lastName)));
        assertTrue(clients.stream().allMatch(clientDto -> clientDto.getBirthdate().equals(localDate)));
        assertEquals(3, clients.size());
    }

    @Test
    void shouldGetAllClientsAndAllActiveClients() {
        ClientEntity clientEntity1 = new ClientEntity("Name1", "last1", "fdd", LocalDate.of(2000,1,1), true);
        ClientEntity clientEntity2 = new ClientEntity("Name2", "last2", "ds", LocalDate.of(2000,1,1), true);
        ClientEntity clientEntity3 = new ClientEntity("Name3", "last3", "asd", LocalDate.of(2000,1,1), false);
        clientRepo.save(clientEntity1);
        clientRepo.save(clientEntity2);
        clientRepo.save(clientEntity3);

        List<ClientDto> allActiveClients = service.getAllActiveClients();
        assertEquals(3, service.getAll().size());
        assertEquals(2, allActiveClients.size());
        assertTrue(allActiveClients.stream().allMatch(ClientDto::isActive));
    }

}