package com.example.demo;

import Dto.LoginDto;
import Dto.RegisterDto;
import com.example.demo.controller.UserController;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    @Test
    void testRegister() {
        RegisterDto dto = new RegisterDto();
        User user = new User();
        when(service.register(dto)).thenReturn(user);

        ResponseEntity<User> response = (ResponseEntity<User>) controller.register(dto);
        assertEquals(user, response.getBody());
    }

    @Test
    void testLogin() {
        LoginDto dto = new LoginDto();
        when(service.authenticate(any(), any())).thenReturn("token");

        ResponseEntity<String> response = controller.login(dto);
        assertEquals("token", response.getBody());
    }
}
