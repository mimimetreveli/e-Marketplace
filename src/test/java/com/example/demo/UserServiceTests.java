package com.example.demo;

import Dto.RegisterDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.TokenUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests{

    @Mock
    private UserRepository repository;

    @Mock
    private TokenUtils tokenUtils;

    @InjectMocks
    private UserService service;

    @Test
    void testRegister() {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("testuser");
        dto.setEmail("test@gmail.com");
        dto.setPassword("1234");
        dto.setConfirmPassword("1234");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));

        when(repository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        User user = service.register(dto);

        assertEquals("testuser", user.getUsername());
        assertEquals("test@gmail.com", user.getEmail());
        assertEquals("1234", user.getPassword());
        verify(repository).save(any());
    }

    @Test
    void testRegister_PasswordMismatch() {
        RegisterDto dto = new RegisterDto();
        dto.setPassword("1234");
        dto.setConfirmPassword("4321");

        assertThrows(IllegalArgumentException.class, () -> service.register(dto));
    }

    @Test
    void testAuthenticate_Success() {
        User user = new User();
        user.setUsername("mimi");
        user.setPassword("1234");
        when(UserRepository.findByUsername("mimi")).thenReturn(Optional.of(user));
        when(tokenUtils.generateToken("mimi")).thenReturn("token");

        String token = service.authenticate("mimi", "1234");
        assertEquals("token", token);
    }

    @Test
    void testAuthenticate_InvalidUser() {
        when(UserRepository.findByUsername("notexist")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.authenticate("notexist", "pw"));
    }

    @Test
    void testAuthenticate_WrongPassword() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("correct");
        when(UserRepository.findByUsername("user")).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> service.authenticate("user", "wrong"));
    }
}