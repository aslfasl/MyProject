package com.example.project.api;

import com.example.project.dto.ClientDto;
import com.example.project.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PostMapping("/client/save")
    public ResponseEntity<ClientDto> saveClientToDatabase(@RequestBody ClientDto clientDto) {
        ClientDto clientDtoResponse = service.saveClient(clientDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/client/save").toUriString());
        return ResponseEntity.created(uri).body(clientDtoResponse);
    }
}
