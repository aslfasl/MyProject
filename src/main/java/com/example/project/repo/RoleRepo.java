package com.example.project.repo;

import com.example.project.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String name);

    boolean existsByName(String name);
}
