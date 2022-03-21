package com.example.project.api;

import com.example.project.dto.ClientDto;
import com.example.project.dto.ClientPage;
import com.example.project.dto.ClientSearchCriteria;
import com.example.project.entity.ClientEntity;
import com.example.project.service.ClientService;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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

    private final ClientService clientService;

    @GetMapping("/client/all")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> list = clientService.getAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/client/all_active")
    public ResponseEntity<List<ClientDto>> getAllActive() {
        return ResponseEntity.ok().body(clientService.getAllActiveClients());
    }

    @PostMapping("/client/save")
    public ResponseEntity<ClientDto> saveClientToDatabase(@RequestBody ClientDto clientDto) {
        ClientDto clientDtoResponse = clientService.saveClient(clientDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/client/save").toUriString());
        return ResponseEntity.created(uri).body(clientDtoResponse);
    }

    @GetMapping("/client/get_by_id/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable long id) {
        return ResponseEntity.ok().body(clientService.getClientById(id));
    }

    @PostMapping("/client/delete/{id}")
    public ResponseEntity<ClientDto> deleteClientById(@PathVariable long id) {
        ClientDto clientDto = clientService.deleteClientById(id);
        return ResponseEntity.accepted().body(clientDto);
    }

    @PatchMapping("/client/update")
    public ResponseEntity<ClientDto> updateClientById(@RequestParam(name = "id") Long id,
                                                      @RequestBody ClientDto clientDto) throws JsonMappingException {
        ClientDto clientReturned =
                clientService.updateClientById(id, clientDto);
        return ResponseEntity.accepted().body(clientReturned);
    }

    @GetMapping("/client/get_by_fullname_birthdate")
    public ResponseEntity<List<ClientDto>> getAllByFullNameAndBirthdate(@RequestParam String firstName,
                                                                        @RequestParam String lastName,
                                                                        @RequestParam
                                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                                LocalDate birthdate) {
        List<ClientDto> clients = clientService.getClientByFullNameAndBirthDate(firstName, lastName, birthdate);
        return ResponseEntity.ok().body(clients);
    }

    @GetMapping("/client/get_by_passport")
    public ResponseEntity<ClientDto> getByPassport(@RequestParam String passport) {
        return ResponseEntity.ok().body(clientService.getClientByPassport(passport));
    }

    @GetMapping("/client/search")
    public ResponseEntity<Page<ClientDto>> getClientsPageFilter(ClientPage clientPage,
                                                                   ClientSearchCriteria clientSearchCriteria) {
        return ResponseEntity.ok().body(clientService.getClientsFilterPage(clientPage, clientSearchCriteria));
    }
}

