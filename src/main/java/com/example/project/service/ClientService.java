package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.entity.ClientEntity;

import java.time.LocalDate;
import java.util.List;


public interface ClientService {

    ClientDto saveClient(ClientDto client);

    ClientDto getClientById(Long id);

    ClientDto deleteClientById(Long id);

    List<ClientDto> getAll();

    ClientDto updateClientById(Long id, String newFirstName, String newLastName,
                               String newPassport, LocalDate newBirthdate, boolean newActive);

    List<ClientDto> getClientByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);

    ClientDto getClientByPassport(String passport);

    List<ClientDto> getAllActiveClients();
}


