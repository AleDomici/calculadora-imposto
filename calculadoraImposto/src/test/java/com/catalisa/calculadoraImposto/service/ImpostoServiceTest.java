package com.catalisa.calculadoraImposto.service;

import com.catalisa.calculadoraImposto.dto.ImpostoRequest;
import com.catalisa.calculadoraImposto.dto.ImpostoResponse;
import com.catalisa.calculadoraImposto.exception.ResourceNotFoundException;
import com.catalisa.calculadoraImposto.model.Imposto;
import com.catalisa.calculadoraImposto.repository.ImpostoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImpostoServiceTest {

    @Mock
    private ImpostoRepository impostoRepository;

    @InjectMocks
    private ImpostoService impostoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastrar_Success() {
        // Arrange
        ImpostoRequest request = new ImpostoRequest("ICMS", "Imposto sobre circulação de mercadorias", 18.0);
        Imposto imposto = new Imposto();
        imposto.setId(1L);
        imposto.setNome(request.getNome());
        imposto.setDescricao(request.getDescricao());
        imposto.setAliquota(request.getAliquota());

        when(impostoRepository.existsByNomeIgnoreCase(request.getNome())).thenReturn(false);
        when(impostoRepository.save(any(Imposto.class))).thenReturn(imposto);

        // Act
        ImpostoResponse response = impostoService.cadastrar(request);

        // Assert
        assertNotNull(response);
        assertEquals("ICMS", response.getNome());
        assertEquals(18.0, response.getAliquota());
        verify(impostoRepository, times(1)).existsByNomeIgnoreCase(request.getNome());
        verify(impostoRepository, times(1)).save(any(Imposto.class));
    }

    @Test
    void testCadastrar_ThrowsException_WhenNomeAlreadyExists() {
        // Arrange
        ImpostoRequest request = new ImpostoRequest("ICMS", "Imposto sobre circulação de mercadorias", 18.0);
        when(impostoRepository.existsByNomeIgnoreCase(request.getNome())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> impostoService.cadastrar(request));
        assertEquals("Já existe um imposto cadastrado com o nome: ICMS", exception.getMessage());
        verify(impostoRepository, times(1)).existsByNomeIgnoreCase(request.getNome());
        verify(impostoRepository, never()).save(any(Imposto.class));
    }

    @Test
    void testBuscarPorId_Success() {
        // Arrange
        Long id = 1L;
        Imposto imposto = new Imposto();
        imposto.setId(id);
        imposto.setNome("ICMS");
        imposto.setDescricao("Imposto sobre circulação de mercadorias");
        imposto.setAliquota(18.0);

        when(impostoRepository.findById(id)).thenReturn(Optional.of(imposto));

        // Act
        ImpostoResponse response = impostoService.buscarPorId(id);

        // Assert
        assertNotNull(response);
        assertEquals("ICMS", response.getNome());
        assertEquals(18.0, response.getAliquota());
        verify(impostoRepository, times(1)).findById(id);
    }

    @Test
    void testBuscarPorId_ThrowsException_WhenIdNotFound() {
        // Arrange
        Long id = 1L;
        when(impostoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> impostoService.buscarPorId(id));
        assertEquals("Imposto não encontrado com ID: 1", exception.getMessage());
        verify(impostoRepository, times(1)).findById(id);
    }

    @Test
    void testCalcularImposto_Success() {
        // Arrange
        Long id = 1L;
        Double valorBase = 1000.0;
        Imposto imposto = new Imposto();
        imposto.setId(id);
        imposto.setNome("ICMS");
        imposto.setAliquota(18.0);

        when(impostoRepository.findById(id)).thenReturn(Optional.of(imposto));

        // Act
        var response = impostoService.calcularImposto(id, valorBase);

        // Assert
        assertNotNull(response);
        assertEquals(180.0, response.get("valorImposto"));
        assertEquals(18.0, response.get("aliquota"));
        assertEquals("ICMS", response.get("nomeImposto"));
        verify(impostoRepository, times(1)).findById(id);
    }

    @Test
    void testCalcularImposto_ThrowsException_WhenValorBaseIsInvalid() {
        // Arrange
        Long id = 1L;
        Double valorBase = -100.0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> impostoService.calcularImposto(id, valorBase));
        assertEquals("O valor base deve ser um valor positivo.", exception.getMessage());
        verify(impostoRepository, never()).findById(anyLong());
    }

    @Test
    void testCalcularImposto_ThrowsException_WhenIdNotFound() {
        // Arrange
        Long id = 1L;
        Double valorBase = 1000.0;
        when(impostoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> impostoService.calcularImposto(id, valorBase));
        assertEquals("Imposto não encontrado com ID: 1", exception.getMessage());
        verify(impostoRepository, times(1)).findById(id);
    }
}