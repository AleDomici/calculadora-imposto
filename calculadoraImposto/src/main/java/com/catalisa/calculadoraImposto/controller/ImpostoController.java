package com.catalisa.calculadoraImposto.controller;

import com.catalisa.calculadoraImposto.dto.CalculoImpostoRequest;
import com.catalisa.calculadoraImposto.dto.ImpostoRequest;
import com.catalisa.calculadoraImposto.dto.ImpostoResponse;
import com.catalisa.calculadoraImposto.exception.ResourceNotFoundException;
import com.catalisa.calculadoraImposto.service.ImpostoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tipos")
public class ImpostoController {

    private final ImpostoService impostoService;

    @Autowired
    public ImpostoController(ImpostoService impostoService) {
        this.impostoService = impostoService;
    }

    @Operation(
            summary = "Excluir um tipo de imposto",
            description = "Exclui um tipo de imposto pelo ID. Acesso restrito ao papel ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imposto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Imposto não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        impostoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar todos os tipos de impostos",
            description = "Retorna uma lista de todos os tipos de impostos cadastrados. Acesso restrito ao papel ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de impostos retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImpostoResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ImpostoResponse>> listarTodos() {
        return ResponseEntity.ok(impostoService.listarTodos());
    }

    @Operation(
            summary = "Cadastrar um novo tipo de imposto",
            description = "Cadastra um novo tipo de imposto. Acesso restrito ao papel ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Imposto cadastrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImpostoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImpostoResponse> cadastrar(@Valid @RequestBody ImpostoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(impostoService.cadastrar(request));
    }

    @Operation(
            summary = "Calcular o valor do imposto",
            description = "Calcula o valor do imposto com base no tipo de imposto e no valor base fornecido. Acesso restrito ao papel ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cálculo realizado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Imposto não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })

    @PostMapping("/calculo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> calcularImposto(@Valid @RequestBody CalculoImpostoRequest request) {
        try {
            Map<String, Object> resultado = impostoService.calcularImposto(request.getTipoImpostoId(), request.getValorBase());
            return ResponseEntity.ok(resultado);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("erro", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Erro interno no servidor: " + e.getMessage()));
        }
    }
}