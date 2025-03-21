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

class RegisterRequestTest {

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
        String expectedRole = "USER";

        // Act
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(expectedUsername);
        registerRequest.setPassword(expectedPassword);
        registerRequest.setRole(expectedRole);

        // Assert
        assertEquals(expectedUsername, registerRequest.getUsername());
        assertEquals(expectedPassword, registerRequest.getPassword());
        assertEquals(expectedRole, registerRequest.getRole());
    }

    @Test
    void testValidationWithValidData() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("validUser");
        registerRequest.setPassword("validPassword");
        registerRequest.setRole("USER");

        // Act
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

        // Assert
        assertTrue(violations.isEmpty(), "There should be no validation errors for valid data.");
    }

    @Test
    void testValidationWithBlankUsername() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("");
        registerRequest.setPassword("validPassword");
        registerRequest.setRole("USER");

        // Act
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

        // Assert
        assertEquals(2, violations.size()); // Esperamos 2 violações: @NotBlank e @Size

        // Verificar se as mensagens de erro esperadas estão presentes
        boolean notBlankViolationFound = violations.stream()
                .anyMatch(v -> v.getMessage().equals("O username é obrigatório.") && v.getPropertyPath().toString().equals("username"));
        boolean sizeViolationFound = violations.stream()
                .anyMatch(v -> v.getMessage().equals("O username deve ter entre 3 e 20 caracteres.") && v.getPropertyPath().toString().equals("username"));

        assertTrue(notBlankViolationFound, "A mensagem de validação @NotBlank não foi encontrada.");
        assertTrue(sizeViolationFound, "A mensagem de validação @Size não foi encontrada.");
    }

    @Test
    void testValidationWithShortUsername() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("ab");
        registerRequest.setPassword("validPassword");
        registerRequest.setRole("USER");

        // Act
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
        assertEquals("O username deve ter entre 3 e 20 caracteres.", violation.getMessage());
        assertEquals("username", violation.getPropertyPath().toString());
    }

    @Test
    void testValidationWithBlankPassword() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("validUser");
        registerRequest.setPassword("");
        registerRequest.setRole("USER");

        // Act
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

        // Assert
        assertEquals(2, violations.size()); // Esperamos 2 violações: @NotBlank e @Size

        boolean notBlankViolationFound = violations.stream()
                .anyMatch(v -> v.getMessage().equals("A senha é obrigatória.") && v.getPropertyPath().toString().equals("password"));
        boolean sizeViolationFound = violations.stream()
                .anyMatch(v -> v.getMessage().equals("A senha deve ter pelo menos 6 caracteres.") && v.getPropertyPath().toString().equals("password"));

        assertTrue(notBlankViolationFound, "A mensagem de validação @NotBlank não foi encontrada.");
        assertTrue(sizeViolationFound, "A mensagem de validação @Size não foi encontrada.");
    }

    @Test
    void testValidationWithShortPassword() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("validUser");
        registerRequest.setPassword("12345");
        registerRequest.setRole("USER");

        // Act
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
        assertEquals("A senha deve ter pelo menos 6 caracteres.", violation.getMessage());
        assertEquals("password", violation.getPropertyPath().toString());
    }

    @Test
    void testValidationWithBlankRole() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("validUser");
        registerRequest.setPassword("validPassword");
        registerRequest.setRole("");

        // Act
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
        assertEquals("O papel (role) é obrigatório.", violation.getMessage());
        assertEquals("role", violation.getPropertyPath().toString());
    }

    @Test
    void testValidationWithAllFieldsBlank() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("");
        registerRequest.setPassword("");
        registerRequest.setRole("");

        // Act
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

        // Assert
        assertEquals(5, violations.size());
    }
}