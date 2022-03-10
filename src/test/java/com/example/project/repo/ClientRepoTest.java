package com.example.project.repo;

import com.example.project.entity.ClientEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientRepoTest {

    @Autowired
    private ClientRepo clientRepo;

    @Test
    void findAllByActive() {
        // TODO: 10.03.2022
        clientRepo.save(new ClientEntity("test", "test", "1234", LocalDate.of(2000,1,1), true));
        clientRepo.save(new ClientEntity("test", "test", "12234", LocalDate.of(2000,1,1), true));
        clientRepo.save(new ClientEntity("test", "test", "12354", LocalDate.of(2000,1,1), false));
        System.out.println(clientRepo.findAllByIsActiveTrue());
    }
}