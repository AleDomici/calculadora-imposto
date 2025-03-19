package com.catalisa.calculadoraImposto.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class SecurityConfigTest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testAuthenticationManagerBean() {
        assertNotNull(authenticationManager, "AuthenticationManager bean deve ser criado.");
    }

    @Test
    public void testPasswordEncoderBean() {
        assertNotNull(passwordEncoder, "PasswordEncoder bean deve ser criado.");
    }
}