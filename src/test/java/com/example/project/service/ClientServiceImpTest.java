package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.ClientEntity;
import com.example.project.repo.ClientRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class ClientServiceImpTest {

    @Autowired
    private ClientServiceImp service;

    @Autowired
    private ClientRepo clientRepo;

    @BeforeEach
    void init(){
        ClientEntity clientEntity1 = new ClientEntity( "NameFirst",
                "SurnameFirst",
                LocalDate.of(2000, 1,1),
                true);
        ClientEntity clientEntity2 = new ClientEntity( "NameSecond",
                "SurnameSecond",
                LocalDate.of(2000, 1,1),
                true);
        clientRepo.save(clientEntity1);
        clientRepo.save(clientEntity2);
        long id1 = clientEntity1.getId();
        long id2 = clientEntity2.getId();
    }

    @AfterEach
    void after() {
        clientRepo.deleteAll();
    }

    @Test
    void shouldSaveClient() {
        Collection<WorkoutDto> workoutsDto = new HashSet<>();
        workoutsDto.add(new WorkoutDto("test training", 90));
        ClientDto clientDto = new ClientDto("Name",
                "Surname",
                LocalDate.of(2000, 1, 1),
                true,
                workoutsDto);
        System.out.println(clientDto);
        service.saveClient(clientDto);

        ClientEntity clientInDb = clientRepo.getClientEntitiesByFirstNameAndLastNameAndBirthdate("Name", "Surname", LocalDate.of(2000, 1, 1));
        System.out.println(clientInDb.getClientWorkouts());
        System.out.println(clientInDb);
        // TODO: 07.03.2022

    }

    @Test
    void getClientById() {
        // TODO: 07.03.2022
    }

    @Test
    void deleteClientById() {
        // TODO: 07.03.2022
    }

    @Test
    void getClientByFullNameAndBirthDate() {
        // TODO: 07.03.2022
        ClientEntity clientEntity = service.getClientByFullNameAndBirthDate("NameFirst",
                "SurnameFirst",
                LocalDate.of(2000, 1, 1));
        System.out.println(clientEntity);
        System.out.println(clientEntity.getClientWorkouts());
    }
}