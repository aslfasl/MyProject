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

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
        Collection<WorkoutDto> workoutsDto = new HashSet<>();
        workoutsDto.add(new WorkoutDto("test training", 90, true, 10, 0,null, null));
        ClientDto clientDto = new ClientDto("Name",
                "Surname",
                "010101",
                LocalDate.of(2000, 1, 1),
                true,
                workoutsDto);
        System.out.println(clientDto);
        service.saveClient(clientDto);

        // TODO: 10.03.2022 Change to list
//        ClientEntity clientInDb = clientRepo.getClientEntitiesByFirstNameAndLastNameAndBirthdate("Name", "Surname", LocalDate.of(2000, 1, 1));
//        System.out.println(clientInDb.getClientWorkouts());
//        System.out.println(clientInDb);
        // TODO: 07.03.2022
    }

    @Test
    void shouldGetClientById() {
        ClientEntity clientEntity = new ClientEntity( "NameFirst",
                "SurnameFirst",
                "414141",
                LocalDate.of(2000, 1,1),
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
    void deleteClientById() {
        // TODO: 07.03.2022
    }

    @Test
    void getClientByFullNameAndBirthDate() {
        // TODO: 07.03.2022 it's list now
//        ClientDto clientDto = service.getClientByFullNameAndBirthDate("NameFirst",
//                "SurnameFirst",
//                LocalDate.of(2000, 1, 1));
//        System.out.println(clientDto);
//        System.out.println(clientDto.getClientWorkouts());
    }

}