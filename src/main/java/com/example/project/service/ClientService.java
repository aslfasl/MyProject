package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.ClientPage;
import com.example.project.dto.ClientSearchCriteria;
import com.example.project.entity.ClientEntity;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;


public interface ClientService {

    ClientDto saveClient(ClientDto client);

    ClientDto getClientById(Long id);

    ClientDto deleteClientById(Long id);

    List<ClientDto> getAll();

    ClientDto updateClientById(Long id, ClientDto clientDto) throws JsonMappingException;

    List<ClientDto> getClientByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);

    ClientDto getClientByPassport(String passport);

    List<ClientDto> getAllActiveClients();

    Page<ClientEntity> getClientsFilterPage(ClientPage clientPage,
                                            ClientSearchCriteria clientSearchCriteria);
}


