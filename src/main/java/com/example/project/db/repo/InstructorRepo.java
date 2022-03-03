package com.example.project.db.repo;

import com.example.project.db.entity.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepo extends JpaRepository<InstructorEntity, Long> {
}
