package com.example.project.repo;

import com.example.project.entity.MembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepo extends JpaRepository<MembershipEntity, Long> {
}
