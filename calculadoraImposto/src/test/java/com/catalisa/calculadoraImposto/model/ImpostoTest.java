package com.catalisa.calculadoraImposto.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImpostoTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Imposto imposto = new Imposto();
        Long expectedId = 1L;
        String expectedNome = "ICMS";
        String expectedDescricao = "Imposto sobre circulação de mercadorias";
        Double expectedAliquota = 18.0;

        // Act
        imposto.setId(expectedId);
        imposto.setNome(expectedNome);
        imposto.setDescricao(expectedDescricao);
        imposto.setAliquota(expectedAliquota);

        // Assert
        assertEquals(expectedId, imposto.getId());
        assertEquals(expectedNome, imposto.getNome());
        assertEquals(expectedDescricao, imposto.getDescricao());
        assertEquals(expectedAliquota, imposto.getAliquota());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Imposto imposto1 = new Imposto();
        imposto1.setId(1L);
        imposto1.setNome("ICMS");
        imposto1.setDescricao("Imposto sobre circulação de mercadorias");
        imposto1.setAliquota(18.0);

        Imposto imposto2 = new Imposto();
        imposto2.setId(1L);
        imposto2.setNome("ICMS");
        imposto2.setDescricao("Imposto sobre circulação de mercadorias");
        imposto2.setAliquota(18.0);

        Imposto imposto3 = new Imposto();
        imposto3.setId(2L);
        imposto3.setNome("IPI");
        imposto3.setDescricao("Imposto sobre produtos industrializados");
        imposto3.setAliquota(10.0);

        // Act & Assert
        assertEquals(imposto1, imposto2); // imposto1 e imposto2 devem ser iguais
        assertNotEquals(imposto1, imposto3); // imposto1 e imposto3 devem ser diferentes
        assertEquals(imposto1.hashCode(), imposto2.hashCode()); // HashCodes devem ser iguais para objetos iguais
        assertNotEquals(imposto1.hashCode(), imposto3.hashCode()); // HashCodes devem ser diferentes para objetos diferentes
    }

    @Test
    void testToString() {
        // Arrange
        Imposto imposto = new Imposto();
        imposto.setId(1L);
        imposto.setNome("ICMS");
        imposto.setDescricao("Imposto sobre circulação de mercadorias");
        imposto.setAliquota(18.0);

        String expectedToString = "Imposto{id=1, nome='ICMS', descricao='Imposto sobre circulação de mercadorias', aliquota=18.0}";

        // Act
        String actualToString = imposto.toString();

        // Assert
        assertEquals(expectedToString, actualToString);
    }
}