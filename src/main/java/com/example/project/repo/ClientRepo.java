package com.example.project.repo;

import com.example.project.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientRepo extends JpaRepository<ClientEntity, Long> {

    List<ClientEntity> getClientEntitiesByFirstNameAndLastNameAndBirthdate(String firstName,
                                                                           String lastName,
                                                                           LocalDate birthdate);

    ClientEntity findByPassport(String passport);

    boolean existsByPassport(String passport);

    @Query(value = "SELECT * FROM client c JOIN membership_info mi ON c.membership_id=mi.id WHERE mi.status = true",
    nativeQuery = true)
    List<ClientEntity> getClientEntitiesByMembershipActive();

    

}
