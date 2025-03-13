package com.catalisa.calculadoraImposto.service;

import com.catalisa.calculadoraImposto.dto.ImpostoResponse;
import com.catalisa.calculadoraImposto.exception.ResourceNotFoundException;
import com.catalisa.calculadoraImposto.model.Imposto;
import com.catalisa.calculadoraImposto.repository.ImpostoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImpostoServiceTest {

    @InjectMocks
    private ImpostoService impostoService;

    @Mock
    private ImpostoRepository impostoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Testes para o método listarTodos
    @Test
    void listarTodos_DeveRetornarListaDeImpostos() {
        // Arrange
        Imposto imposto1 = criarImposto(1L, "ICMS", "Imposto sobre Circulação de Mercadorias e Serviços", 18.0);
        Imposto imposto2 = criarImposto(2L, "ISS", "Imposto sobre Serviços", 5.0);
        when(impostoRepository.findAll()).thenReturn(Arrays.asList(imposto1, imposto2));

        // Act
        List<ImpostoResponse> result = impostoService.listarTodos();

        // Assert
        assertEquals(2, result.size());
        assertEquals("ICMS", result.get(0).getNome());
        assertEquals("ISS", result.get(1).getNome());
    }

    // Testes para o método excluir
    @Test
    void excluir_DeveExcluirImpostoQuandoExistir() {
        // Arrange
        Long id = 1L;
        Imposto imposto = criarImposto(id, "ICMS", "Imposto sobre Circulação de Mercadorias e Serviços", 18.0);
        when(impostoRepository.findById(id)).thenReturn(Optional.of(imposto));
        doNothing().when(impostoRepository).deleteById(id);

        // Act
        impostoService.excluir(id);

        // Assert
        verify(impostoRepository, times(1)).findById(id);
        verify(impostoRepository, times(1)).deleteById(id);
    }

    @Test
    void excluir_DeveLancarExcecaoQuandoImpostoNaoExistir() {
        // Arrange
        Long id = 1L;
        when(impostoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> impostoService.excluir(id));
        verify(impostoRepository, times(1)).findById(id);
        verify(impostoRepository, never()).deleteById(id);
    }

    // Métodos auxiliares
    private Imposto criarImposto(Long id, String nome, String descricao, double aliquota) {
        Imposto imposto = new Imposto();
        imposto.setId(id);
        imposto.setNome(nome);
        imposto.setDescricao(descricao);
        imposto.setAliquota(aliquota);
        return imposto;
    }
}