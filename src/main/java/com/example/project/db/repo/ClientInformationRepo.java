package com.example.project.db.repo;

import com.example.project.db.entity.ClientInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientInformationRepo extends JpaRepository<ClientInfoEntity, Long> {
}
