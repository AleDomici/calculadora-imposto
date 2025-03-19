package com.catalisa.calculadoraImposto.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.catalisa.calculadoraImposto.model.Users;

class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsername_ShouldReturnUser_WhenUsernameExists() {
        // Arrange
        String username = "testUser";
        Users user = new Users();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword("password123");
        user.setRole("USER");

        // Simula o comportamento do repositório
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        Optional<Users> result = userRepository.findByUsername(username);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().getUsername());
        assertEquals(user.getPassword(), result.get().getPassword());
        assertEquals(user.getRole(), result.get().getRole());
    }

    @Test
    void testFindByUsername_ShouldReturnEmpty_WhenUsernameDoesNotExist() {
        // Arrange
        String username = "nonExistentUser";

        // Simula o comportamento do repositório
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        Optional<Users> result = userRepository.findByUsername(username);

        // Assert
        assertTrue(result.isEmpty());
    }
}
