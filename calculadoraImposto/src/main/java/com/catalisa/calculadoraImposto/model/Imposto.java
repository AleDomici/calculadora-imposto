package com.catalisa.calculadoraImposto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "imposto")
public class Imposto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do imposto é obrigatório.")
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @NotBlank(message = "A descrição do imposto é obrigatória.")
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @NotNull(message = "A alíquota do imposto é obrigatória.")
    @Positive(message = "A alíquota deve ser um valor positivo.")
    @Column(name = "aliquota", nullable = false)
    private Double aliquota;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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