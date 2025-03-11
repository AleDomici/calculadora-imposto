package com.catalisa.calculadoraImposto.repository;

import com.catalisa.calculadoraImposto.model.Imposto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpostoRepository extends JpaRepository<Imposto, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}