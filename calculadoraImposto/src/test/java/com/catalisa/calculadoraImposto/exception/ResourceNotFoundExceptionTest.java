package com.catalisa.calculadoraImposto.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        // Arrange
        String expectedMessage = "Resource not found";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testExceptionThrown() {
        // Arrange
        String expectedMessage = "Resource not found";

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    throw new ResourceNotFoundException(expectedMessage);
                }
        );

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }
}
