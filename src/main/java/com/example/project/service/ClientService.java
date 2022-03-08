package com.example.project.service;

import com.example.project.db.entity.ClientEntity;
import com.example.project.db.repo.ClientRepo;
import com.example.project.dto.ClientDto;
import com.example.project.dto.Converter;
import com.example.project.dto.WorkoutDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public interface ClientService {

    ClientEntity saveClient(ClientEntity client);
    ClientEntity getClientById(Long id);
    void deleteClientById(Long id);
    List<ClientEntity> getAll();

    // TODO: 07.03.2022
//    ClientEntity updateClientById(Long id);

    ClientEntity getClientByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);

}
