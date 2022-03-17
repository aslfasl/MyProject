package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.converter.Converter;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.example.project.repo.ClientRepo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.project.exception.ExceptionMessageUtils.*;

@Service
@AllArgsConstructor
@Transactional
public class ClientServiceImp implements ClientService {

    private final ClientRepo clientRepo;
    private final Converter converter;
    private final ObjectMapper objectMapper;

    public void addWorkoutToClient(ClientEntity clientEntity, WorkoutEntity workout) {
        if (clientEntity.getClientWorkouts().contains(workout)) {
            throw new CustomException(CLIENT_ALREADY_SIGNED_FOR + workout.getName(), ErrorType.ALREADY_EXISTS);
        }
        clientEntity.getClientWorkouts().add(workout);
        workout.getClients().add(clientEntity);
    }

    @Override
    public ClientDto saveClient(ClientDto clientDto) {
        if (clientRepo.existsByPassport(clientDto.getPassport())) {
            throw new CustomException(CLIENT_ALREADY_EXISTS_PASSPORT + clientDto.getPassport(),
                    ErrorType.ALREADY_EXISTS);
        }
        ClientEntity clientEntity = clientRepo.save(converter.convertClientDto(clientDto));
        return converter.convertClientEntity(clientEntity);
    }

    @Override
    public ClientDto getClientById(Long id) {
        ClientEntity clientEntity = clientRepo.findClientById(id);
        if (clientEntity == null) {
            throw new CustomException(CLIENT_NOT_FOUND_ID + id,
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

    // TODO: 16.03.2022 use this approach to reduce verbose in com.example.project.service.ClientServiceImp.updateClientById
    // FIXED. This is how it looks now.
    @Override
    public ClientDto updateClientById(Long id, String newFirstName, String newLastName,
                                      String newPassport, LocalDate newBirthdate, boolean newActive) throws JsonMappingException {
        Optional<ClientEntity> optionalClientEntity = clientRepo.findById(id);
        ClientEntity clientOverride =
                new ClientEntity(newFirstName, newLastName, newPassport, newBirthdate, newActive);
        if (optionalClientEntity.isEmpty()) {
            throw new CustomException(CLIENT_NOT_FOUND_ID + id,
                    ErrorType.NOT_FOUND);
        }
        ClientEntity clientToUpdate = optionalClientEntity.get();
        if (clientRepo.existsByPassport(newPassport)) {
            throw new CustomException(CLIENT_ALREADY_EXISTS_PASSPORT + newPassport,
                    ErrorType.ALREADY_EXISTS);
        } else {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            clientToUpdate = objectMapper.updateValue(clientToUpdate, clientOverride);
        }
        return converter.convertClientEntity(clientRepo.save(clientToUpdate));
    }


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
            throw new CustomException(CLIENT_NOT_FOUND_PASSPORT + passport,
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
