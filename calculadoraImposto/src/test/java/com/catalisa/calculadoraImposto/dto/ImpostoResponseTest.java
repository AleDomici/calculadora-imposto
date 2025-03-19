package com.catalisa.calculadoraImposto.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImpostoResponseTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        Long expectedId = 1L;
        String expectedNome = "Imposto de Renda";
        String expectedDescricao = "Imposto sobre a renda";
        Double expectedAliquota = 27.5;

        // Act
        ImpostoResponse impostoResponse = new ImpostoResponse(expectedId, expectedNome, expectedDescricao, expectedAliquota);

        // Assert
        assertEquals(expectedId, impostoResponse.getId());
        assertEquals(expectedNome, impostoResponse.getNome());
        assertEquals(expectedDescricao, impostoResponse.getDescricao());
        assertEquals(expectedAliquota, impostoResponse.getAliquota());
    }

    @Test
    void testSetters() {
        // Arrange
        ImpostoResponse impostoResponse = new ImpostoResponse();
        Long newId = 2L;
        String newNome = "ICMS";
        String newDescricao = "Imposto sobre circulação de mercadorias";
        Double newAliquota = 18.0;

        // Act
        impostoResponse.setId(newId);
        impostoResponse.setNome(newNome);
        impostoResponse.setDescricao(newDescricao);
        impostoResponse.setAliquota(newAliquota);

        // Assert
        assertEquals(newId, impostoResponse.getId());
        assertEquals(newNome, impostoResponse.getNome());
        assertEquals(newDescricao, impostoResponse.getDescricao());
        assertEquals(newAliquota, impostoResponse.getAliquota());
    }
}