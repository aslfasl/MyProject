package com.example.project.repo;

import com.example.project.entity.ClientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//class ClientRepoTest {
//
//    @Autowired
//    private ClientRepo clientRepo;
//
//    @BeforeEach
//    void setUp(){
//        clientRepo.deleteAll();
//    }
//
//    @Test
//    void findAllByActiveIsTrue() {
//        clientRepo.save(new ClientEntity("test", "test", "1234", LocalDate.of(2000,1,1), true));
//        clientRepo.save(new ClientEntity("test", "test", "12234", LocalDate.of(2000,1,1), true));
//        clientRepo.save(new ClientEntity("test", "test", "12354", LocalDate.of(2000,1,1), false));
//
//        List<ClientEntity> allByIsActiveTrue = clientRepo.findAllByIsActiveTrue();
//
//        assertTrue(allByIsActiveTrue.stream().allMatch(ClientEntity::isActive));
//        assertEquals(2, allByIsActiveTrue.size());
//    }
//}