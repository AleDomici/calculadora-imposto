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

import java.util.*;

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

    // Teste para listar todos os impostos
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

    // Teste para cadastrar um novo imposto
    @Test
    void cadastrar_DeveSalvarEDevolverImposto() {
        // Arrange
        ImpostoRequest request = new ImpostoRequest("IPI", "Imposto sobre Produtos Industrializados", 10.0);
        Imposto impostoSalvo = criarImposto(1L, "IPI", "Imposto sobre Produtos Industrializados", 10.0);
        when(impostoRepository.existsByNomeIgnoreCase(request.getNome())).thenReturn(false);
        when(impostoRepository.save(any(Imposto.class))).thenReturn(impostoSalvo);

        // Act
        ImpostoResponse response = impostoService.cadastrar(request);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals("IPI", response.getNome());
        assertEquals("Imposto sobre Produtos Industrializados", response.getDescricao());
        assertEquals(10.0, response.getAliquota());
        verify(impostoRepository, times(1)).save(any(Imposto.class));
    }

    @Test
    void cadastrar_DeveLancarExcecaoQuandoNomeJaExistir() {
        // Arrange
        ImpostoRequest request = new ImpostoRequest("IPI", "Imposto sobre Produtos Industrializados", 10.0);
        when(impostoRepository.existsByNomeIgnoreCase(request.getNome())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> impostoService.cadastrar(request));
        assertEquals("Já existe um imposto cadastrado com o nome: IPI", exception.getMessage());
        verify(impostoRepository, never()).save(any(Imposto.class));
    }

    // Teste para buscar imposto por ID
    @Test
    void buscarPorId_DeveRetornarImpostoQuandoExistir() {
        // Arrange
        Long id = 1L;
        Imposto imposto = criarImposto(id, "ICMS", "Imposto sobre Circulação de Mercadorias e Serviços", 18.0);
        when(impostoRepository.findById(id)).thenReturn(Optional.of(imposto));

        // Act
        ImpostoResponse response = impostoService.buscarPorId(id);

        // Assert
        assertEquals(id, response.getId());
        assertEquals("ICMS", response.getNome());
        assertEquals("Imposto sobre Circulação de Mercadorias e Serviços", response.getDescricao());
        assertEquals(18.0, response.getAliquota());
    }

    @Test
    void buscarPorId_DeveLancarExcecaoQuandoNaoExistir() {
        // Arrange
        Long id = 1L;
        when(impostoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> impostoService.buscarPorId(id));
        assertEquals("Imposto não encontrado com ID: " + id, exception.getMessage());
    }

    // Teste para excluir imposto por ID
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
    void excluir_DeveLancarExcecaoQuandoNaoExistir() {
        // Arrange
        Long id = 1L;
        when(impostoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> impostoService.excluir(id));
        assertEquals("Imposto não encontrado com ID: " + id, exception.getMessage());
        verify(impostoRepository, never()).deleteById(id);
    }

    // Teste para calcular imposto
    @Test
    void calcularImposto_DeveRetornarValoresCalculados() {
        // Arrange
        Long id = 1L;
        Double valorBase = 100.0;
        Imposto imposto = criarImposto(id, "ICMS", "Imposto sobre Circulação de Mercadorias e Serviços", 18.0);
        when(impostoRepository.findById(id)).thenReturn(Optional.of(imposto));

        // Act
        Map<String, Object> response = impostoService.calcularImposto(id, valorBase);

        // Assert
        assertEquals(18.0, response.get("valorImposto"));
        assertEquals(id, response.get("tipoImpostoId"));
        assertEquals(valorBase, response.get("valorBase"));
        assertEquals(18.0, response.get("aliquota"));
        assertEquals("ICMS", response.get("nomeImposto"));
    }

    @Test
    void calcularImposto_DeveLancarExcecaoQuandoValorBaseInvalido() {
        // Arrange
        Long id = 1L;
        Double valorBase = -100.0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> impostoService.calcularImposto(id, valorBase));
        assertEquals("O valor base deve ser um valor positivo.", exception.getMessage());
    }

    // Método auxiliar para criar um objeto Imposto
    private Imposto criarImposto(Long id, String nome, String descricao, double aliquota) {
        Imposto imposto = new Imposto();
        imposto.setId(id);
        imposto.setNome(nome);
        imposto.setDescricao(descricao);
        imposto.setAliquota(aliquota);
        return imposto;
    }
}