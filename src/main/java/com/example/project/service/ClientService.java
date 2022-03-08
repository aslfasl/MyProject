package com.example.project.service;

import com.example.project.entity.ClientEntity;

import java.time.LocalDate;
import java.util.List;


public interface ClientService {

    ClientEntity saveClient(ClientEntity client);
    ClientEntity getClientById(Long id);
    void deleteClientById(Long id);
    List<ClientEntity> getAll();

    // TODO: 07.03.2022
//    ClientEntity updateClientById(Long id);

    ClientEntity getClientByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);

}
