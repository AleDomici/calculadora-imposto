package com.catalisa.calculadoraImposto.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CalculoImpostoRequest {

    @NotNull(message = "O campo 'tipoImpostoId' é obrigatório.")
    private Long tipoImpostoId;

    @NotNull(message = "O campo 'valorBase' é obrigatório.")
    @Positive(message = "O campo 'valorBase' deve ser maior que zero.")
    private Double valorBase;

    // Getters e Setters
    public Long getTipoImpostoId() {
        return tipoImpostoId;
    }

    public void setTipoImpostoId(Long tipoImpostoId) {
        this.tipoImpostoId = tipoImpostoId;
    }

    public Double getValorBase() {
        return valorBase;
    }

    public void setValorBase(Double valorBase) {
        this.valorBase = valorBase;
    }
}