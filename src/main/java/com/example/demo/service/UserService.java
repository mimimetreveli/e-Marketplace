package com.example.demo.service;

import Dto.LoginDto;
import Dto.RegisterDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final TokenUtils tokenUtils;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo, TokenUtils tokenUtils) {
        this.repo = repo;
        this.tokenUtils = tokenUtils;
    }

    public User register(RegisterDto dto) {
        if (!dto.password.equals(dto.confirmPassword))
            throw new RuntimeException("Passwords do not match");

        if (repo.existsByUsername(dto.username) || repo.existsByEmail(dto.email))
            throw new RuntimeException("User already exists");

        User user = new User();
        user.setUsername(dto.username);
        user.setEmail(dto.email);
        user.setPassword(encoder.encode(dto.password));
        user.setDateOfBirth(dto.dateOfBirth);
        repo.save(user);
        return user;
    }

    public String login(LoginDto dto) {
        User user = UserRepository.findByUsername(dto.username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(dto.password, user.getPassword()))
            throw new RuntimeException("Wrong password");

        return tokenUtils.generateToken(user.getUsername());
    }

    public String authenticate(String username, String password) {
        User user = UserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return tokenUtils.generateToken(username);
    }

    public User findByUsername(String username) {
        return UserRepository.findByUsername(username).orElseThrow();
    }
}
