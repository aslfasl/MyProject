package com.example.project.service;

import com.example.project.entity.ClientEntity;
import com.example.project.repo.ClientRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

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
        service.saveClient(clientEntity1);
        service.saveClient(clientEntity2);
        long id1 = clientEntity1.getId();
        long id2 = clientEntity2.getId();
    }

    @AfterEach
    void after() {
        clientRepo.deleteAll();
    }

    @Test
    void saveClient() {
        ClientEntity clientEntity = new ClientEntity( "Name",
                "Surname",
                LocalDate.of(2000, 1,1),
                true);
        service.saveClient(clientEntity);
        long id = clientEntity.getId();
        // TODO: 07.03.2022

        System.out.println(clientRepo.findById(id));
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