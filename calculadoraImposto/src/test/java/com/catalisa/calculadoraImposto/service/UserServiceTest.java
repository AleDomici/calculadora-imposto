package com.catalisa.calculadoraImposto.service;

import com.catalisa.calculadoraImposto.dto.RegisterRequest;
import com.catalisa.calculadoraImposto.dto.RegisterResponse;
import com.catalisa.calculadoraImposto.model.Users;
import com.catalisa.calculadoraImposto.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("securepassword");
        request.setRole("USER");

        Users savedUser = new Users();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("hashedpassword");
        savedUser.setRole("USER");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedpassword");
        when(userRepository.save(any(Users.class))).thenReturn(savedUser);

        // Act
        RegisterResponse response = userService.registerUser(request);

        // Assert
        assertNotNull(response);
        assertEquals("Usuário registrado com sucesso!", response.getMessage());
        assertEquals(1L, response.getUserId());
        assertEquals("USER", response.getUsername());

        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(any(Users.class));
    }
}
