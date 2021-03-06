package com.example.project.api;

import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutClassDto;
import com.example.project.entity.InstructorEntity;
import com.example.project.repo.InstructorRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.HashSet;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithMockUser(username = "username", authorities = {"ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER"})
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class InstructorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InstructorRepo instructorRepo;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();

        instructorRepo.deleteAll();
    }

    @AfterEach
    void tearDown() {
        instructorRepo.deleteAll();
    }

    @Test
    void shouldGetInstructorById() throws Exception {
        InstructorEntity instructorEntity =
                new InstructorEntity("Paul", "Green", "200", LocalDate.of(2000, 1, 1), true);
        instructorRepo.save(instructorEntity);
        long id = instructorEntity.getId();

        mockMvc.perform(get("/api/instructor/by_id/" + id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", equalTo("Paul")))
                .andExpect(jsonPath("$.lastName", equalTo("Green")))
                .andExpect(jsonPath("$.passport", equalTo("200")));
    }


    @Test
    void shouldMakeInstructorInactiveWhenDeleteById() throws Exception {
        InstructorEntity instructorEntity =
                new InstructorEntity("Paul", "Green", "200", LocalDate.of(2000, 1, 1), true);
        instructorRepo.save(instructorEntity);
        long id = instructorEntity.getId();

        mockMvc.perform(post("/api/instructor/delete/" + id))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.active", equalTo(false)));
    }

    @Test
    void shouldSaveInstructorToDatabase() throws Exception {
        InstructorDto instructorDto = new InstructorDto(null,
                "Jack",
                "Black",
                "passport1",
                "address",
                "123456",
                "speciality",
                "education",
                true,
                LocalDate.of(1999, 1, 1),
                new HashSet<>());
        WorkoutClassDto workoutClassDto = new WorkoutClassDto(null, "sport",
                "description", true, 15, null, null, null);
        instructorDto.getInstructorWorkouts().add(workoutClassDto);

        mockMvc.perform(post("/api/instructor/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instructorDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.passport", equalTo("passport1")))
                .andExpect(jsonPath("$.firstName", equalTo("Jack")))
                .andExpect(jsonPath("$.instructorWorkouts", hasSize(1)));
        assertTrue(instructorRepo.existsByPassport("passport1"));
    }

    @Test
    void shouldGetInstructorByPassport() throws Exception {
        String passport = "20120";
        InstructorEntity instructorEntity =
                new InstructorEntity("George", "Brown", passport, LocalDate.of(2000, 1, 1), true);
        instructorRepo.save(instructorEntity);

        mockMvc.perform(get("/api/instructor/by_passport?passport={passport}", passport))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", equalTo("George")))
                .andExpect(jsonPath("$.lastName", equalTo("Brown")))
                .andExpect(jsonPath("$.passport", equalTo(passport)));
    }

    @Test
    void shouldGetListOfAllActiveInstructors() throws Exception {
        InstructorEntity instructorEntity1 = new InstructorEntity("Baron",
                "Wulf", "qer", LocalDate.of(1950, 1, 1), true);
        InstructorEntity instructorEntity2 = new InstructorEntity("Lord",
                "Bee", "3gq", LocalDate.of(1950, 1, 1), true);
        InstructorEntity instructorEntity3 = new InstructorEntity("Lord",
                "Josh", "6hh", LocalDate.of(1950, 1, 1), false);
        instructorRepo.save(instructorEntity1);
        instructorRepo.save(instructorEntity2);
        instructorRepo.save(instructorEntity3);

        mockMvc.perform(get("/api/instructor/all_active"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].active", contains(true, true)));
    }

    @Test
    void shouldGetListOfAllClients() throws Exception {
        InstructorEntity instructorEntity1 = new InstructorEntity("Baron",
                "Wulf", "qer", LocalDate.of(1950, 1, 1), true);
        InstructorEntity instructorEntity2 = new InstructorEntity("Lord",
                "Bee", "3gq", LocalDate.of(1950, 1, 1), true);
        InstructorEntity instructorEntity3 = new InstructorEntity("Pop",
                "Josh", "6hh", LocalDate.of(1950, 1, 1), false);
        instructorRepo.save(instructorEntity1);
        instructorRepo.save(instructorEntity2);
        instructorRepo.save(instructorEntity3);

        mockMvc.perform(get("/api/instructor/all"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].active", contains(true, true, false)))
                .andExpect(jsonPath("$[*].firstName", contains("Baron", "Lord", "Pop")));
    }

    @Test
    void shouldUpdateInstructorById() throws Exception {
        InstructorEntity instructorEntity = new InstructorEntity("Paul", "Green", "200", LocalDate.of(2000, 1, 1), true);
        instructorRepo.save(instructorEntity);
        long id = instructorEntity.getId();
        String newFirstname = "Anna", newLastname = "Ivanova", newPassport = "fffda123",
                newAddress = "address", newPhone = "123456", newSpeciality = "speciality", newEducation = "education";
        LocalDate newBirthdate = LocalDate.of(1995, 5, 5);
        boolean newActive = true;
        InstructorDto instructorDto = new InstructorDto(
                null,
                newFirstname,
                newLastname,
                newPassport,
                newAddress,
                newPhone,
                newSpeciality,
                newEducation,
                newActive,
                newBirthdate,
                null);

        String content = mockMvc.perform((patch("/api/instructor/update?id={id}", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instructorDto)))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", equalTo(newFirstname)))
                .andExpect(jsonPath("$.lastName", equalTo(newLastname)))
                .andExpect(jsonPath("$.passport", equalTo(newPassport)))
                .andExpect(jsonPath("$.active", equalTo(newActive)))
                .andExpect(jsonPath("$.education", equalTo(newEducation)))
                .andExpect(jsonPath("$.address", equalTo(newAddress)))
                .andExpect(jsonPath("$.speciality", equalTo(newSpeciality)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        InstructorDto instructorContent = objectMapper.readValue(content, InstructorDto.class);
        assertEquals(newBirthdate, instructorContent.getBirthdate());
    }
}