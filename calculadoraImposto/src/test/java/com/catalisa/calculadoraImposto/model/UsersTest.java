package com.catalisa.calculadoraImposto.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Users user = new Users();
        Long expectedId = 1L;
        String expectedUsername = "testUser";
        String expectedPassword = "password123";
        String expectedRole = "ADMIN";

        // Act
        user.setId(expectedId);
        user.setUsername(expectedUsername);
        user.setPassword(expectedPassword);
        user.setRole(expectedRole);

        // Assert
        assertEquals(expectedId, user.getId());
        assertEquals(expectedUsername, user.getUsername());
        assertEquals(expectedPassword, user.getPassword());
        assertEquals(expectedRole, user.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Users user1 = new Users();
        user1.setId(1L);
        user1.setUsername("testUser");
        user1.setPassword("password123");
        user1.setRole("ADMIN");

        Users user2 = new Users();
        user2.setId(1L);
        user2.setUsername("testUser");
        user2.setPassword("password123");
        user2.setRole("ADMIN");

        Users user3 = new Users();
        user3.setId(2L);
        user3.setUsername("anotherUser");
        user3.setPassword("password456");
        user3.setRole("USER");

        // Act & Assert
        assertEquals(user1, user2); // user1 e user2 devem ser iguais
        assertNotEquals(user1, user3); // user1 e user3 não devem ser iguais
        assertEquals(user1.hashCode(), user2.hashCode()); // hashCode de user1 e user2 devem ser iguais
        assertNotEquals(user1.hashCode(), user3.hashCode()); // hashCode de user1 e user3 não devem ser iguais
    }

    @Test
    void testToString() {
        // Arrange
        Users user = new Users();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setRole("ADMIN");

        // Act
        String toStringResult = user.toString();

        // Assert
        String expected = "Users{id=1, username='testUser', password='password123', role='ADMIN'}";
        assertEquals(expected, toStringResult);
    }
}