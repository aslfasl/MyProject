package com.example.project.api;

import com.example.project.dto.ClientDto;
import com.example.project.entity.ClientEntity;
import com.example.project.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("client")
@AllArgsConstructor
public class ClientController {

    private final ClientService service;

    @GetMapping("get_all")
    public ResponseEntity<List<ClientDto>> getAllClientsById() {
        List<ClientDto> list = service.getAll();
        return ResponseEntity.ok().body(list);
    }
}
