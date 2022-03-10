package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.entity.ClientEntity;

import java.time.LocalDate;
import java.util.List;


public interface ClientService {

    ClientEntity saveClient(ClientDto client);
    ClientDto getClientById(Long id);
    void deleteClientById(Long id);
    List<ClientDto> getAll();

    // TODO: 07.03.2022
//    ClientEntity updateClientById(Long id);

    ClientDto getClientByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);
    ClientDto getClientByPassport(String passport);

}
