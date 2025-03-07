package com.catalisa.calculadoraImposto.service;

import com.catalisa.calculadoraImposto.dto.RegisterRequest;
import com.catalisa.calculadoraImposto.dto.RegisterResponse;
import com.catalisa.calculadoraImposto.model.Users;
import com.catalisa.calculadoraImposto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        // Criação do usuário
        Users user = new Users();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Criptografa a senha
        user.setRole(registerRequest.getRole());

        // Salva o usuário no banco de dados
        Users savedUser = userRepository.save(user);


        return new RegisterResponse(
                "Usuário registrado com sucesso!",
                savedUser.getId(),
                savedUser.getRole()
        );
    }
}