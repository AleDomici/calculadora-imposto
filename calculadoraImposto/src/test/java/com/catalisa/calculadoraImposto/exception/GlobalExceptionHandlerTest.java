package com.catalisa.calculadoraImposto.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleValidationExceptions() {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindException bindException = mock(BindException.class);
        when(exception.getBindingResult()).thenReturn(bindException);

        FieldError fieldError1 = new FieldError("impostoRequest", "field1", "Field1 is invalid");
        FieldError fieldError2 = new FieldError("impostoRequest", "field2", "Field2 is required");
        when(bindException.getFieldErrors()).thenReturn(java.util.List.of(fieldError1, fieldError2));

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("field1", "Field1 is invalid");
        expectedErrors.put("field2", "Field2 is required");
        assertEquals(expectedErrors, response.getBody());
    }

    @Test
    void testHandleGeneralExceptions() {
        // Arrange
        Exception exception = new Exception("Unexpected error occurred");

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleGeneralExceptions(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno: Unexpected error occurred", response.getBody());
    }
}
