package com.example.demo.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationConfigurationTest {

    private final ApplicationConfiguration config = new ApplicationConfiguration();

    @Test
    void testBCryptPasswordEncoderBean() {
        BCryptPasswordEncoder encoder = config.bCryptPasswordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder.matches("test", encoder.encode("test")));
    }

    @Test
    void testSecurityContextRepositoryBean() {
        SecurityContextRepository repo = config.securityContextRepository();
        assertNotNull(repo);
    }
}
