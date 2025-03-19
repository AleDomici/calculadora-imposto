package com.catalisa.calculadoraImposto.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        String expectedUsername = "testUser";
        String expectedPassword = "testPassword";

        // Act
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(expectedUsername);
        loginRequest.setPassword(expectedPassword);

        // Assert
        assertEquals(expectedUsername, loginRequest.getUsername());
        assertEquals(expectedPassword, loginRequest.getPassword());
    }

    @Test
    void testValidationWithValidData() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("validUser");
        loginRequest.setPassword("validPassword");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertTrue(violations.isEmpty(), "There should be no validation errors for valid data.");
    }

    @Test
    void testValidationWithBlankUsername() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("");
        loginRequest.setPassword("validPassword");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<LoginRequest> violation = violations.iterator().next();
        assertEquals("O username é obrigatório.", violation.getMessage());
        assertEquals("username", violation.getPropertyPath().toString());
    }

    @Test
    void testValidationWithBlankPassword() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("validUser");
        loginRequest.setPassword("");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<LoginRequest> violation = violations.iterator().next();
        assertEquals("A senha é obrigatória.", violation.getMessage());
        assertEquals("password", violation.getPropertyPath().toString());
    }

    @Test
    void testValidationWithBothFieldsBlank() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("");
        loginRequest.setPassword("");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertEquals(2, violations.size());
    }
}