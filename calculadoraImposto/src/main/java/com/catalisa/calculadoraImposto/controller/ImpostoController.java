package com.catalisa.calculadoraImposto.controller;

import com.catalisa.calculadoraImposto.dto.ImpostoRequest;
import com.catalisa.calculadoraImposto.dto.ImpostoResponse;
import com.catalisa.calculadoraImposto.service.ImpostoService;
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

    // Apenas ADMIN pode listar todos os impostos
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ImpostoResponse>> listarTodos() {
        return ResponseEntity.ok(impostoService.listarTodos());
    }

    // Apenas ADMIN pode cadastrar um novo imposto
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImpostoResponse> cadastrar(@Valid @RequestBody ImpostoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(impostoService.cadastrar(request));
    }

    // Apenas ADMIN pode buscar um imposto por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImpostoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(impostoService.buscarPorId(id));
    }

    // Apenas ADMIN pode excluir um imposto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        impostoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/calculo")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Map<String, Object>> calcularImposto(@RequestBody Map<String, Object> request) {
        // Validação dos dados de entrada
        if (!request.containsKey("tipoImpostoId") || !request.containsKey("valorBase")) {
            return ResponseEntity.badRequest().body(Map.of(
                    "erro", "Os campos 'tipoImpostoId' e 'valorBase' são obrigatórios."
            ));
        }

        try {

            Long tipoImpostoId = Long.valueOf(request.get("tipoImpostoId").toString());
            Double valorBase = Double.valueOf(request.get("valorBase").toString());


            Map<String, Object> resultado = impostoService.calcularImposto(tipoImpostoId, valorBase);

            return ResponseEntity.ok(Map.of(
                    "tipoImpostoId", tipoImpostoId,
                    "valorBase", valorBase,
                    "valorImposto", resultado.get("valorImposto")
            ));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "erro", "Os campos 'tipoImpostoId' e 'valorBase' devem ser numéricos."
            ));
        }
    }
}