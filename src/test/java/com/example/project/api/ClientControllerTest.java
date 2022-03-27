package com.example.project.api;

import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.MembershipDto;
import com.example.project.dto.WorkoutClassDto;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.MembershipEntity;
import com.example.project.repo.ClientRepo;
import com.example.project.repo.MembershipRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RequiredArgsConstructor
@AutoConfigureMockMvc
@WithMockUser(username = "username", authorities = {"ROLE_MANAGER"})
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private MembershipRepo membershipRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        clientRepo.deleteAll();
    }

    @Test
    void shouldGetListOfAllActiveClients() throws Exception {
        ClientEntity clientEntity1 = new ClientEntity(
                "Baron",
                "Wulf",
                "qer",
                LocalDate.of(1950, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        ClientEntity clientEntity2 = new ClientEntity(
                "Lord",
                "Bee",
                "3gq",
                LocalDate.of(1950, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        ClientEntity clientEntity3 = new ClientEntity(
                "Lord",
                "Josh",
                "6hh",
                LocalDate.of(1950, 1, 1),
                new MembershipEntity(null, null, false),
                new HashSet<>());
        clientRepo.save(clientEntity1);
        clientRepo.save(clientEntity2);
        clientRepo.save(clientEntity3);

        mockMvc.perform(get("/api/client/all_active"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].membership.active", contains(true, true)));
    }

    @Test
    void shouldGetListOfAllClients() throws Exception {
        ClientEntity clientEntity1 = new ClientEntity(
                "Baron",
                "Wulf",
                "qer",
                LocalDate.of(1950, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        ClientEntity clientEntity2 = new ClientEntity(
                "Lord",
                "Bee",
                "3gq",
                LocalDate.of(1950, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
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
        ClientDto clientDto = new ClientDto(
                null,
                "Jack",
                "Black",
                "passport1",
                "address1",
                "123456",
                LocalDate.of(1999, 1, 1),
                new MembershipDto(null, null, true),
                new HashSet<>());
        InstructorDto instructorDto = new InstructorDto(
                null,
                "Iname",
                "Ilastname",
                "df3135as",
                "address",
                "1345",
                "spec",
                "edu",
                true,
                LocalDate.of(1999, 9, 9),
                new HashSet<>());
        WorkoutClassDto workoutClassDto = new WorkoutClassDto(null, "sport", "description",
                true, 15, new HashSet<>(), instructorDto, new HashSet<>());
        clientDto.getClientWorkouts().add(workoutClassDto);

        mockMvc.perform(post("/api/client/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.passport", equalTo("passport1")))
                .andExpect(jsonPath("$.firstName", equalTo("Jack")))
                .andExpect(jsonPath("$.clientWorkouts.[0].name", equalTo("sport")));
    }

    @Test
    void shouldGetClientById() throws Exception {
        ClientEntity clientEntity = new ClientEntity(
                "Paul",
                "Green",
                "200",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        clientRepo.save(clientEntity);
        long id = clientEntity.getId();

        mockMvc.perform(get("/api/client/get_by_id/" + id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", equalTo("Paul")))
                .andExpect(jsonPath("$.lastName", equalTo("Green")))
                .andExpect(jsonPath("$.passport", equalTo("200")));
    }

    @Test
    void shouldHandleException() throws Exception {
        long id = -1204;

        mockMvc.perform(get("/api/client/get_by_id/" + id))
                .andExpect(jsonPath("$.code", equalTo(-100)))
                .andExpect(jsonPath("$.name", equalTo("NOT_FOUND")))
                .andExpect(jsonPath("$.message", equalTo("Could not client with id: -1204")))
                .andDo(print());
    }

    @Test
    void shouldChangeIsActiveToFalseWhenDeleteById() throws Exception {
        ClientEntity clientEntity = new ClientEntity(
                "Paul",
                "Green",
                "200",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, true),
                new HashSet<>());
        clientRepo.save(clientEntity);
        long id = clientEntity.getId();

        mockMvc.perform(post("/api/client/delete/" + id))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.membership.active", equalTo(false)));
    }

    @Test
    void shouldUpdateClientById() throws Exception {
        ClientEntity clientEntity = new ClientEntity(
                "Paul",
                "Green",
                "200",
                LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, false),
                new HashSet<>());
        clientRepo.save(clientEntity);
        long id = clientEntity.getId();

        String newFirstname = "Anna", newLastname = "Ivanova", newPassport = "fffda123",
                newAddress = "address", newPhone = "123456";
        LocalDate newBirthdate = LocalDate.of(1995, 5, 5),
                newStartDate = LocalDate.of(1999, 9, 9),
                newEndDate = LocalDate.of(2001, 1, 1);
        ClientDto clientDto = new ClientDto(null,
                newFirstname,
                newLastname,
                newPassport,
                newAddress,
                newPhone,
                newBirthdate,
                new MembershipDto(
                        newStartDate,
                        newEndDate,
                        true),
                null);

        String content = mockMvc.perform((patch("/api/client/update?id={id}", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", equalTo(newFirstname)))
                .andExpect(jsonPath("$.lastName", equalTo(newLastname)))
                .andExpect(jsonPath("$.passport", equalTo(newPassport)))
                .andExpect(jsonPath("$.membership.active", equalTo(true)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ClientDto clientReturned = objectMapper.readValue(content, ClientDto.class);
        assertEquals(newBirthdate, clientReturned.getBirthdate());
        assertEquals(newStartDate, clientReturned.getMembership().getStartDate());
        assertEquals(newEndDate, clientReturned.getMembership().getEndDate());
    }

    @Test
    void shouldGetAllClientsWithTheSameFirstNameLastNameAndBirthdate() throws Exception {
        String name = "Bob";
        String lastName = "Lee";
        LocalDate birthdate = LocalDate.of(2000, 1, 1);
        ClientEntity clientEntity1 = new ClientEntity(name, lastName, "414141", birthdate,
                new MembershipEntity(null, null, true), new HashSet<>());
        ClientEntity clientEntity2 = new ClientEntity(name, lastName, "3333", birthdate,
                new MembershipEntity(null, null, true), new HashSet<>());
        ClientEntity clientEntity3 = new ClientEntity(name, lastName, "12111", birthdate,
                new MembershipEntity(null, null, true), new HashSet<>());
        clientRepo.save(clientEntity1);
        clientRepo.save(clientEntity2);
        clientRepo.save(clientEntity3);

        String content = mockMvc.perform(
                        get("/api/client/get_by_fullname_birthdate?firstName={name}&lastName={lastName}&birthdate={birthdate}",
                                name, lastName, birthdate))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].firstName", containsInAnyOrder(name, name, name)))
                .andExpect(jsonPath("$[*].lastName", containsInAnyOrder(lastName, lastName, lastName)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ClientDto> clients = Arrays.asList(objectMapper.readValue(content, ClientDto[].class));
        assertTrue(clients.stream().allMatch(clientDto -> clientDto.getBirthdate().isEqual(birthdate)));
    }

    @Test
    void shouldGetClientByPassport() throws Exception {
        String passport = "20120";
        ClientEntity clientEntity = new ClientEntity("George", "Brown", passport, LocalDate.of(2000, 1, 1),
                new MembershipEntity(null, null, true), new HashSet<>());
        clientRepo.save(clientEntity);

        mockMvc.perform(get("/api/client/get_by_passport?passport={passport}", passport))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", equalTo("George")))
                .andExpect(jsonPath("$.lastName", equalTo("Brown")))
                .andExpect(jsonPath("$.passport", equalTo(passport)));
    }
}