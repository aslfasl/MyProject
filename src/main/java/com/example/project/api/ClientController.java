package com.example.project.api;

import com.example.project.dto.ClientDto;
import com.example.project.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api")
public class ClientController {

    private final ClientService service;

    @GetMapping("client/all")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> list = service.getAll();
        return ResponseEntity.ok().body(list);
    }
}
