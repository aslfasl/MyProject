package com.example.project.api;

import com.example.project.dto.ClientDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.entity.ClientEntity;
import com.example.project.repo.ClientRepo;
import com.example.project.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RequiredArgsConstructor
@AutoConfigureMockMvc
@WithMockUser
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
    void shouldGetListOfAllClients() throws Exception {
        ClientEntity clientEntity1 = new ClientEntity("Baron",
                "Wulf", "qer", LocalDate.of(1950, 1, 1), true);
        ClientEntity clientEntity2 = new ClientEntity("Lord",
                "Bee", "3gq", LocalDate.of(1950, 1, 1), true);
        clientRepo.save(clientEntity1);
        clientRepo.save(clientEntity2);

        mockMvc.perform(get("/api/client/all"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].passport", containsInAnyOrder("qer", "3gq")))
                .andExpect(jsonPath("$[*].firstName", containsInAnyOrder("Baron", "Lord")));
    }

    @Test
    void shouldSaveNewClientToDatabase() throws Exception {
        ClientDto clientDto =
                new ClientDto("Jack",
                        "Black",
                        "passport1",
                        LocalDate.of(1999, 1, 1),
                        true,
                        new HashSet<>());
        WorkoutDto workoutDto = new WorkoutDto("sport", 45, true, 15, null, null);
        clientDto.getClientWorkouts().add(workoutDto);

        mockMvc.perform(post("/api/client/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.passport", equalTo("passport1")))
                .andExpect(jsonPath("$.firstName", equalTo("Jack")));
    }
}