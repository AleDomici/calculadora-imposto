package com.catalisa.calculadoraImposto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ImpostoRequest {

    @NotBlank(message = "O campo 'nome' é obrigatório e não pode estar vazio.")
    private String nome;

    @NotBlank(message = "O campo 'descricao' é obrigatório e não pode estar vazio.")
    private String descricao;

    @NotNull(message = "O campo 'aliquota' é obrigatório.")
    @Positive(message = "O campo 'aliquota' deve ser maior que zero.")
    private Double aliquota;

    public ImpostoRequest() {
    }

    public ImpostoRequest(String nome, String descricao, Double aliquota) {
        this.nome = nome;
        this.descricao = descricao;
        this.aliquota = aliquota;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getAliquota() {
        return aliquota;
    }

    public void setAliquota(Double aliquota) {
        this.aliquota = aliquota;
    }
}