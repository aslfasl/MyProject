package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.Converter;
import com.example.project.entity.ClientEntity;
import com.example.project.repo.ClientRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ClientServiceImp implements ClientService {

    private final ClientRepo clientRepo;
    private final Converter converter;

    @Override
    public ClientEntity saveClient(ClientDto clientDto) {
        ClientEntity clientEntity = converter.convertClientDto(clientDto);
        return clientRepo.save(clientEntity);
    }

    @Override
    public ClientDto getClientById(Long id) {
        ClientEntity clientEntity = clientRepo.findClientById(id);
        return converter.convertClientEntity(clientEntity);
    }

    @Override
    public List<ClientDto> getAll(){
        return clientRepo.findAll()
                .stream()
                .map(converter::convertClientEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteClientById(Long id) {
        clientRepo.deleteById(id);
    }

    // TODO: 07.03.2022
//    @Override
//    public ClientEntity updateClientById() {
//        return null;
//    }

    @Override
    public List<ClientDto> getClientByFullNameAndBirthDate(String firstName,
                                                              String lastName,
                                                              LocalDate birthdate) {

        return clientRepo.getClientEntitiesByFirstNameAndLastNameAndBirthdate(firstName, lastName, birthdate).stream()
                .map(converter::convertClientEntity)
                .collect(Collectors.toList());

    }

    @Override
    public ClientDto getClientByPassport(String passport) {
        return converter.convertClientEntity(clientRepo.findClientEntityByPassport(passport));
    }
}
