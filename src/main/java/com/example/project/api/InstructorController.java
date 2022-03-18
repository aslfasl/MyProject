package com.example.project.api;

import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.service.InstructorService;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @GetMapping("/instructor/by_id/{id}")
    public ResponseEntity<InstructorDto> getInstructorById(@PathVariable long id) {
        return ResponseEntity.ok().body(instructorService.getById(id));
    }

    @GetMapping("/instructor/by_fullname/")
    public ResponseEntity<List<InstructorDto>> getInstructorById(@RequestParam String firstname, @RequestParam String lastname) {
        return ResponseEntity.ok().body(instructorService.getByFullName(firstname, lastname));
    }

    @PostMapping("/instructor/delete/{id}")
    public ResponseEntity<InstructorDto> makeInstructorInactive(@PathVariable Long id) {
        return ResponseEntity.accepted().body(instructorService.deleteById(id));
    }

    @PostMapping("/instructor/save")
    public ResponseEntity<InstructorDto> saveInstructorToDatabase(@RequestBody InstructorDto instructorDto) {
        URI uri =
                URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/instructor/save").toUriString());
        return ResponseEntity.created(uri).body(instructorService.save(instructorDto));
    }

    @GetMapping("/instructor/by_passport")
    public ResponseEntity<InstructorDto> getByPassport(@RequestParam String passport) {
        return ResponseEntity.ok().body(instructorService.getByPassport(passport));
    }

    @GetMapping("/instructor/all_active")
    public ResponseEntity<List<InstructorDto>> getAllActive() {
        return ResponseEntity.ok().body(instructorService.getAllActive());
    }

    @GetMapping("/instructor/all")
    public ResponseEntity<List<InstructorDto>> getAllClients() {
        List<InstructorDto> list = instructorService.getAll();
        return ResponseEntity.ok().body(list);
    }

    @PatchMapping("/instructor/update")
    public ResponseEntity<InstructorDto> updateClientById(@RequestParam(name = "id") Long id,
                                                          @RequestBody InstructorDto instructor) throws JsonMappingException {
        InstructorDto instructorUpdated =
                instructorService.updateById(id, instructor);
        return ResponseEntity.accepted().body(instructorUpdated);
    }
}
