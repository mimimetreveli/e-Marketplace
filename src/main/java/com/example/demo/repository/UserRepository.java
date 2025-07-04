package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    static Optional<User> findByUsername(String username) {
        return null;
    }

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
