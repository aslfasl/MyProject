package com.example.project.repo;

import com.example.project.entity.ClientEntity;
import com.example.project.entity.MembershipEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientRepoTest {

    @Autowired
    private ClientRepo clientRepo;

    @BeforeEach
    void setUp(){
        clientRepo.deleteAll();
    }

    @Test
    void getClientEntitiesByMembershipActive() {
        clientRepo.save(new ClientEntity("First", "null", "121", null,
                new MembershipEntity(null, null, true), new HashSet<>()));
        clientRepo.save(new ClientEntity("Second", "null", "122", null,
                new MembershipEntity(null, null, true), new HashSet<>()));
        clientRepo.save(new ClientEntity("Third", null, "123", null,
                new MembershipEntity(null, null, true), new HashSet<>()));
        clientRepo.save(new ClientEntity("Fourth", null, "124", null,
                new MembershipEntity(null, null, false), new HashSet<>()));
        clientRepo.save(new ClientEntity("Fifth", null, "125", null,
                new MembershipEntity(null, null, false), new HashSet<>()));

        List<ClientEntity> clientsActive = clientRepo.getClientEntitiesByMembershipActive();

        assertTrue(clientsActive.stream().allMatch(client -> client.getMembership().isActive()));
        System.out.println(clientsActive);
        assertEquals(3, clientsActive.size());

    }
}