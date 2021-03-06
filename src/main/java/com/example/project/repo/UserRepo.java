package com.example.project.repo;

import com.example.project.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    boolean existsByUsername(String username);

}
