package com.example.demo.service;

import Dto.LoginDto;
import Dto.RegisterDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.TokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository repo;
    @Mock
    private TokenUtils tokenUtils;
    @InjectMocks
    private UserService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        RegisterDto dto = new RegisterDto("user", "email@test.com", "password", "password", LocalDate.now());
        when(repo.existsByUsername("user")).thenReturn(false);
        when(repo.existsByEmail("email@test.com")).thenReturn(false);

        User result = service.register(dto);
        assertEquals("user", result.getUsername());
        verify(repo).save(any());
    }

    @Test
    void testRegisterPasswordMismatch() {
        RegisterDto dto = new RegisterDto("user", "email", "123", "456", LocalDate.now());
        assertThrows(RuntimeException.class, () -> service.register(dto));
    }

    @Test
    void testLoginSuccess() {
        LoginDto dto = new LoginDto("user", "123");
        User user = new User();
        user.setUsername("user");
        user.setPassword(new BCryptPasswordEncoder().encode("123"));

        when(UserRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(tokenUtils.generateToken("user")).thenReturn("token");

        String token = service.login(dto);
        assertEquals("token", token);
    }

    @Test
    void testLoginInvalidPassword() {
        LoginDto dto = new LoginDto("user", "wrong");
        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode("right"));

        when(UserRepository.findByUsername("user")).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> service.login(dto));
    }
}
