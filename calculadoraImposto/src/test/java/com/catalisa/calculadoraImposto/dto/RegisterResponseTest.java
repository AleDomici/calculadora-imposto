package com.catalisa.calculadoraImposto.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegisterResponseTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String expectedMessage = "User registered successfully";
        Long expectedUserId = 1L;
        String expectedUsername = "testUser";

        // Act
        RegisterResponse response = new RegisterResponse(expectedMessage, expectedUserId, expectedUsername);

        // Assert
        assertEquals(expectedMessage, response.getMessage());
        assertEquals(expectedUserId, response.getUserId());
        assertEquals(expectedUsername, response.getUsername());
    }

    @Test
    void testSetters() {
        // Arrange
        RegisterResponse response = new RegisterResponse(null, null, null);

        String newMessage = "Updated message";
        Long newUserId = 2L;
        String newUsername = "updatedUser";

        // Act
        response.setMessage(newMessage);
        response.setUserId(newUserId);
        response.setUsername(newUsername);

        // Assert
        assertEquals(newMessage, response.getMessage());
        assertEquals(newUserId, response.getUserId());
        assertEquals(newUsername, response.getUsername());
    }
}