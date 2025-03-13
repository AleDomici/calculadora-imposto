package com.catalisa.calculadoraImposto.service;

import com.catalisa.calculadoraImposto.dto.ImpostoResponse;
import com.catalisa.calculadoraImposto.model.Imposto;
import com.catalisa.calculadoraImposto.repository.ImpostoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ImpostoServiceTest {

    @InjectMocks
    private ImpostoService impostoService;

    @Mock
    private ImpostoRepository impostoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_DeveRetornarListaDeImpostos() {
        // Arrange
        Imposto imposto1 = new Imposto();
        imposto1.setId(1L);
        imposto1.setNome("ICMS");
        imposto1.setDescricao("Imposto sobre Circulação de Mercadorias e Serviços");
        imposto1.setAliquota(18.0);

        Imposto imposto2 = new Imposto();
        imposto2.setId(2L);
        imposto2.setNome("ISS");
        imposto2.setDescricao("Imposto sobre Serviços");
        imposto2.setAliquota(5.0);

        when(impostoRepository.findAll()).thenReturn(Arrays.asList(imposto1, imposto2));

        // Act
        List<ImpostoResponse> result = impostoService.listarTodos();

        // Assert
        assertEquals(2, result.size());
        assertEquals("ICMS", result.get(0).getNome());
        assertEquals("ISS", result.get(1).getNome());
    }
}