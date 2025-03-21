package com.catalisa.calculadoraImposto.controller;

import com.catalisa.calculadoraImposto.dto.LoginRequest;
import com.catalisa.calculadoraImposto.dto.RegisterRequest;
import com.catalisa.calculadoraImposto.dto.RegisterResponse;
import com.catalisa.calculadoraImposto.service.UserService;
import com.catalisa.calculadoraImposto.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Registrar um novo usuário", description = "Registra um novo usuário no sistema. O username deve ser único e a senha será armazenada de forma segura.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = userService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Autenticar usuário", description = "Autentica um usuário e retorna um token JWT para acesso aos endpoints protegidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\" }"))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        if (authentication.getAuthorities() == null || !authentication.getAuthorities().iterator().hasNext()) {
            throw new IllegalStateException("Authentication failed or authorities are missing");
        }

        String token = jwtUtil.generateToken(
                authentication.getName(),
                authentication.getAuthorities().iterator().next().getAuthority()
        );

        if (token == null) {
            throw new IllegalStateException("Token generation failed");
        }

        return ResponseEntity.ok(Map.of("token", token));
    }
}