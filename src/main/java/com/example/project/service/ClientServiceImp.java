package com.example.project.service;

import ch.qos.logback.core.net.server.Client;
import com.example.project.db.entity.ClientEntity;
import com.example.project.db.repo.ClientRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ClientServiceImp implements ClientService {

    private final ClientRepo clientRepo;

    @Override
    public ClientEntity saveClient(ClientEntity client) {
        return clientRepo.save(client);
    }

    @Override
    public ClientEntity getClientById(Long id) {
        return clientRepo.findClientById(id);
    }

    @Override
    public List<ClientEntity> getAll(){
        return clientRepo.findAll();
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
    public ClientEntity getClientByFullNameAndBirthDate(String firstName,
                                                              String lastName,
                                                              LocalDate birthdate) {

        return clientRepo.getClientEntitiesByFirstNameAndLastNameAndBirthdate(firstName,
                lastName,
                birthdate);
    }
}
