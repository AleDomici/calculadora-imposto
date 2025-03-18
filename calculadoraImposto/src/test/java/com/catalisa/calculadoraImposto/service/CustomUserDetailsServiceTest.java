package com.catalisa.calculadoraImposto.service;

import com.catalisa.calculadoraImposto.model.Users;
import com.catalisa.calculadoraImposto.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        // Arrange
        String username = "testuser";
        Users user = new Users();
        user.setUsername(username);
        user.setPassword("hashedpassword");
        user.setRole("USER");

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails, "O UserDetails não deve ser nulo.");
        assertEquals(username, userDetails.getUsername(), "O username deve ser igual ao fornecido.");
        assertEquals("hashedpassword", userDetails.getPassword(), "A senha deve ser igual à armazenada.");
        assertTrue(userDetails.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")),
                "O usuário deve ter a autoridade ROLE_USER.");
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });

        assertEquals("Usuário não encontrado: " + username, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }
}
