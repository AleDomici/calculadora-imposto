package com.catalisa.calculadoraImposto.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // Configurando a chave secreta manualmente para o teste
        jwtUtil.setSecretKey("aB1cD2eF3gH4iJ5kL6mN7oP8qR9sT0uVwXyZ!@#%&*()-_=+?");
    }

    @Test
    void testGenerateToken_Success() {
        // Arrange
        String username = "testuser";
        String role = "ADMIN";

        // Act
        String token = jwtUtil.generateToken(username, role);

        // Assert
        assertNotNull(token, "O token não deve ser nulo.");
        assertTrue(jwtUtil.validateToken(token), "O token gerado deve ser válido.");
        assertEquals(username, jwtUtil.getUsername(token), "O username extraído deve ser igual ao fornecido.");
        assertEquals(role, jwtUtil.getRole(token), "O role extraído deve ser igual ao fornecido.");
    }

    @Test
    void testValidateToken_ValidToken() {
        // Arrange
        String token = jwtUtil.generateToken("testuser", "USER");

        // Act
        boolean isValid = jwtUtil.validateToken(token);

        // Assert
        assertTrue(isValid, "O token deve ser válido.");
    }

    @Test
    void testValidateToken_InvalidToken() {
        // Arrange
        String invalidToken = "tokenInvalido";

        // Act
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid, "O token inválido não deve ser validado.");
    }

    @Test
    void testGetClaims_Success() {
        // Arrange
        String username = "testuser";
        String role = "USER";
        String token = jwtUtil.generateToken(username, role);

        // Act
        Claims claims = jwtUtil.getClaims(token);

        // Assert
        assertNotNull(claims, "As claims não devem ser nulas.");
        assertEquals(username, claims.getSubject(), "O username nas claims deve ser igual ao fornecido.");
        assertEquals(role, claims.get("role"), "O role nas claims deve ser igual ao fornecido.");
    }

    @Test
    void testGetUsername_Success() {
        // Arrange
        String username = "testuser";
        String token = jwtUtil.generateToken(username, "USER");

        // Act
        String extractedUsername = jwtUtil.getUsername(token);

        // Assert
        assertEquals(username, extractedUsername, "O username extraído deve ser igual ao fornecido.");
    }

    @Test
    void testGetRole_Success() {
        // Arrange
        String role = "ADMIN";
        String token = jwtUtil.generateToken("testuser", role);

        // Act
        String extractedRole = jwtUtil.getRole(token);

        // Assert
        assertEquals(role, extractedRole, "O role extraído deve ser igual ao fornecido.");
    }
}