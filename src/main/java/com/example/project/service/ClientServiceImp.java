package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.dto.Converter;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
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
    public ClientDto saveClient(ClientDto clientDto) {
        if (clientRepo.existsByPassport(clientDto.getPassport())) {
            throw new CustomException("Client with passport" + clientDto.getPassport() + " already exists",
                    ErrorType.ALREADY_EXISTS);
        }
        ClientEntity clientEntity = clientRepo.save(converter.convertClientDto(clientDto));
        return converter.convertClientEntity(clientEntity);
    }

    @Override
    public ClientDto getClientById(Long id) {
        ClientEntity clientEntity = clientRepo.findClientById(id);
        if (clientEntity == null) {
            throw new CustomException("Client with id " + id + " not found",
                    ErrorType.NOT_FOUND);
        }
        return converter.convertClientEntity(clientEntity);
    }

    @Override
    public ClientDto deleteClientById(Long id) {
        ClientEntity clientEntity = clientRepo.findClientById(id);
        clientEntity.setActive(false);
        clientRepo.save(clientEntity);
        return converter.convertClientEntity(clientEntity);
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
        ClientEntity client = clientRepo.findClientEntityByPassport(passport);
        if (client == null) {
            throw new CustomException("Client with passport " + passport + " not found",
                    ErrorType.NOT_FOUND);
        }
        return converter.convertClientEntity(client);
    }

    @Override
    public List<ClientDto> getAllActiveClients() {
        return clientRepo.findAllByIsActiveTrue().stream()
                .map(converter::convertClientEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientDto> getAll() {
        return clientRepo.findAll()
                .stream()
                .map(converter::convertClientEntity)
                .collect(Collectors.toList());
    }
}
