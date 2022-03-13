package com.example.project.api;

import com.example.project.dto.ClientDto;
import com.example.project.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api")
public class ClientController {

    private final ClientService service;

    @GetMapping("/client/all")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> list = service.getAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/client/all_active")
    public ResponseEntity<List<ClientDto>> getAllActive(){
        return ResponseEntity.ok().body(service.getAllActiveClients());
    }

    @PostMapping("/client/save")
    public ResponseEntity<ClientDto> saveClientToDatabase(@RequestBody ClientDto clientDto) {
        ClientDto clientDtoResponse = service.saveClient(clientDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/client/save").toUriString());
        return ResponseEntity.created(uri).body(clientDtoResponse);
    }

    @GetMapping("/client/get_by_id/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable long id) {
        return ResponseEntity.ok().body(service.getClientById(id));
    }

    @PostMapping("/client/delete/{id}")
    public ResponseEntity<ClientDto> deleteClientById(@PathVariable long id) {
        ClientDto clientDto = service.deleteClientById(id);
        return ResponseEntity.accepted().body(clientDto);
    }

    @GetMapping("/client/get_by_fullname_birthdate")
    public ResponseEntity<List<ClientDto>> getAllByFullNameAndBirthdate(@RequestParam String firstName,
                                                                        @RequestParam String lastName,
                                                                        @RequestParam
                                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                                LocalDate birthdate) {
        List<ClientDto> clients = service.getClientByFullNameAndBirthDate(firstName, lastName, birthdate);
        return ResponseEntity.ok().body(clients);
    }

    @GetMapping("/client/get_by_passport")
    public ResponseEntity<ClientDto> getByPassport(@RequestParam String passport){
        return ResponseEntity.ok().body(service.getClientByPassport(passport));
    }
}

