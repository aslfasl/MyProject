package com.example.project.api;

import com.example.project.dto.ClientDto;
import com.example.project.entity.ClientEntity;
import com.example.project.repo.ClientRepo;
import com.example.project.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RequiredArgsConstructor
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientService service;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    @WithMockUser
    void getAllClients() throws Exception {
        ClientEntity clientEntity1 = new ClientEntity("Baron",
                "Wulf", "qer", LocalDate.of(1950, 1,1), true);
        ClientEntity clientEntity2 = new ClientEntity("Lord",
                "Bee", "3gq", LocalDate.of(1950, 1,1), true);
        clientRepo.save(clientEntity1);
        clientRepo.save(clientEntity2);

        mockMvc.perform(get("/api/client/all"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].passport", containsInAnyOrder("qer", "3gq")))
                .andExpect(jsonPath("$[*].firstName", containsInAnyOrder("Baron", "Lord")));
    }
}