package com.example.project.service;

import com.example.project.dto.ClientDto;
import com.example.project.converter.Converter;
import com.example.project.dto.ClientPage;
import com.example.project.dto.ClientSearchCriteria;
import com.example.project.entity.ClientEntity;
import com.example.project.entity.WorkoutEntity;
import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.example.project.repo.ClientCriteriaRepo;
import com.example.project.repo.ClientRepo;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.project.exception.ExceptionMessageUtils.*;

@Service
@AllArgsConstructor
@Transactional
public class ClientServiceImp implements ClientService {

    private final ClientRepo clientRepo;
    private final ClientCriteriaRepo clientCriteriaRepo;
    private final Converter converter;
    private final ObjectMapper objectMapper;
    private final ValidationService validationService;

    public void addWorkoutToClient(ClientEntity clientEntity, WorkoutEntity workout) {
        validationService.checkIsWorkoutAvailable(workout);
        if (clientEntity.getClientWorkouts().contains(workout)) {
            throw new CustomException(CLIENT_ALREADY_SIGNED_FOR + workout.getName(), ErrorType.ALREADY_EXISTS);
        }
        clientEntity.getClientWorkouts().add(workout);
        workout.getClients().add(clientEntity);
    }

    @Override
    public ClientDto saveClient(ClientDto clientDto) {
        validationService.checkClientAge(clientDto);
        validationService.checkClientStatus(clientDto);
        if (clientRepo.existsByPassport(clientDto.getPassport())) {
            throw new CustomException(CLIENT_ALREADY_EXISTS_PASSPORT + clientDto.getPassport(),
                    ErrorType.ALREADY_EXISTS);
        }
        ClientEntity clientEntity = clientRepo.save(converter.convertClientDto(clientDto));
        return converter.convertClientEntity(clientEntity);
    }

    @Override
    public ClientDto getClientById(Long id) {
        return converter.convertClientEntity(clientRepo.findById(id)
                .orElseThrow(() -> new CustomException(CLIENT_NOT_FOUND_ID + id, ErrorType.NOT_FOUND)));
    }

    @Override
    public ClientDto deleteClientById(Long id) {
        ClientEntity clientEntity = clientRepo.findClientById(id);
        clientEntity.setActive(false);
        clientRepo.save(clientEntity);
        return converter.convertClientEntity(clientEntity);
    }

    @Override
    public ClientDto updateClientById(Long id, ClientDto clientOverride) throws JsonMappingException {
        ClientEntity clientToUpdate = clientRepo.findById(id)
                .orElseThrow(() -> new CustomException(CLIENT_NOT_FOUND_ID + id, ErrorType.NOT_FOUND));
        if (clientRepo.existsByPassport(clientOverride.getPassport())) {
            throw new CustomException(CLIENT_ALREADY_EXISTS_PASSPORT + clientOverride.getPassport(),
                    ErrorType.ALREADY_EXISTS);
        } else {
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

    public Page<ClientDto> getClientsFilterPage(ClientPage clientPage,
                                                ClientSearchCriteria clientSearchCriteria) {
        return clientCriteriaRepo.findAllWithFilters(clientPage, clientSearchCriteria);
    }

}
