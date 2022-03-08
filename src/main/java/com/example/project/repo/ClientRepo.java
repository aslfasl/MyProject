package com.example.project.repo;

import com.example.project.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ClientRepo extends JpaRepository<ClientEntity, Long> {

    @Query(value = "SELECT c FROM ClientEntity c " +
            "LEFT JOIN FETCH c.clientWorkouts " +
            "WHERE c.id = ?1")
    ClientEntity findClientById(long clientId);

    ClientEntity getClientEntitiesByFirstNameAndLastNameAndBirthdate(String firstName,
                                                                           String lastName,
                                                                           LocalDate birthdate);

    ClientEntity findClientEntityByPassport(String passport);

}
