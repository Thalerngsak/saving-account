package com.example.saving.account.repository;

import java.util.Optional;

import com.example.saving.account.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCitizenId(String citizenId);
}
