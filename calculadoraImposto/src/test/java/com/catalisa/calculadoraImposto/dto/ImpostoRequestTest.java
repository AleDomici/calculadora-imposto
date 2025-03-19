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

class ImpostoRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        String expectedNome = "Imposto de Renda";
        String expectedDescricao = "Imposto sobre a renda";
        Double expectedAliquota = 27.5;

        // Act
        ImpostoRequest impostoRequest = new ImpostoRequest();
        impostoRequest.setNome(expectedNome);
        impostoRequest.setDescricao(expectedDescricao);
        impostoRequest.setAliquota(expectedAliquota);

        // Assert
        assertEquals(expectedNome, impostoRequest.getNome());
        assertEquals(expectedDescricao, impostoRequest.getDescricao());
        assertEquals(expectedAliquota, impostoRequest.getAliquota());
    }

    @Test
    void testValidationWithValidData() {
        // Arrange
        ImpostoRequest impostoRequest = new ImpostoRequest("Imposto de Renda", "Imposto sobre a renda", 27.5);

        // Act
        Set<ConstraintViolation<ImpostoRequest>> violations = validator.validate(impostoRequest);

        // Assert
        assertTrue(violations.isEmpty(), "There should be no validation errors for valid data.");
    }

    @Test
    void testValidationWithBlankNome() {
        // Arrange
        ImpostoRequest impostoRequest = new ImpostoRequest("", "Imposto sobre a renda", 27.5);

        // Act
        Set<ConstraintViolation<ImpostoRequest>> violations = validator.validate(impostoRequest);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<ImpostoRequest> violation = violations.iterator().next();
        assertEquals("O campo 'nome' é obrigatório e não pode estar vazio.", violation.getMessage());
        assertEquals("nome", violation.getPropertyPath().toString());
    }

    @Test
    void testValidationWithBlankDescricao() {
        // Arrange
        ImpostoRequest impostoRequest = new ImpostoRequest("Imposto de Renda", "", 27.5);

        // Act
        Set<ConstraintViolation<ImpostoRequest>> violations = validator.validate(impostoRequest);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<ImpostoRequest> violation = violations.iterator().next();
        assertEquals("O campo 'descricao' é obrigatório e não pode estar vazio.", violation.getMessage());
        assertEquals("descricao", violation.getPropertyPath().toString());
    }

    @Test
    void testValidationWithNullAliquota() {
        // Arrange
        ImpostoRequest impostoRequest = new ImpostoRequest("Imposto de Renda", "Imposto sobre a renda", null);

        // Act
        Set<ConstraintViolation<ImpostoRequest>> violations = validator.validate(impostoRequest);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<ImpostoRequest> violation = violations.iterator().next();
        assertEquals("O campo 'aliquota' é obrigatório.", violation.getMessage());
        assertEquals("aliquota", violation.getPropertyPath().toString());
    }

    @Test
    void testValidationWithNegativeAliquota() {
        // Arrange
        ImpostoRequest impostoRequest = new ImpostoRequest("Imposto de Renda", "Imposto sobre a renda", -5.0);

        // Act
        Set<ConstraintViolation<ImpostoRequest>> violations = validator.validate(impostoRequest);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<ImpostoRequest> violation = violations.iterator().next();
        assertEquals("O campo 'aliquota' deve ser maior que zero.", violation.getMessage());
        assertEquals("aliquota", violation.getPropertyPath().toString());
    }

    @Test
    void testValidationWithAllFieldsInvalid() {
        // Arrange
        ImpostoRequest impostoRequest = new ImpostoRequest("", "", -5.0);

        // Act
        Set<ConstraintViolation<ImpostoRequest>> violations = validator.validate(impostoRequest);

        // Assert
        assertEquals(3, violations.size());
    }
}