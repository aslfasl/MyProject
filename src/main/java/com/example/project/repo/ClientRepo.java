package com.example.project.repo;

import com.example.project.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientRepo extends JpaRepository<ClientEntity, Long> {

    @Query(value = "SELECT c FROM ClientEntity c " +
            "LEFT JOIN FETCH c.clientWorkouts " +
            "WHERE c.id = ?1")
    ClientEntity findClientById(long clientId);

    List<ClientEntity> getClientEntitiesByFirstNameAndLastNameAndBirthdate(String firstName,
                                                                           String lastName,
                                                                           LocalDate birthdate);

    ClientEntity findClientEntityByPassport(String passport);

    List<ClientEntity> findAllByIsActiveTrue();
    List<ClientEntity> findAllByIsActiveFalse(); // TODO: 10.03.2022
    boolean existsByPassport(String passport);
    

}
