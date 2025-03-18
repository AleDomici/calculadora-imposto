package com.catalisa.calculadoraImposto.service;

import com.catalisa.calculadoraImposto.dto.ImpostoRequest;
import com.catalisa.calculadoraImposto.dto.ImpostoResponse;
import com.catalisa.calculadoraImposto.exception.ResourceNotFoundException;
import com.catalisa.calculadoraImposto.model.Imposto;
import com.catalisa.calculadoraImposto.repository.ImpostoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImpostoService {

    private final ImpostoRepository impostoRepository;

    @Autowired
    public ImpostoService(ImpostoRepository impostoRepository) {
        this.impostoRepository = impostoRepository;
    }

    // Listar todos os impostos
    public List<ImpostoResponse> listarTodos() {
        return impostoRepository.findAll().stream()
                .map(this::convertToResponse) // Usando método auxiliar para conversão
                .collect(Collectors.toList());
    }

    // Cadastrar um novo imposto
    public ImpostoResponse cadastrar(ImpostoRequest request) {
        // Validação: Verificar se o nome já existe no banco de dados
        if (impostoRepository.existsByNomeIgnoreCase(request.getNome())) {
            throw new IllegalArgumentException("Já existe um imposto cadastrado com o nome: " + request.getNome());
        }

        // Validação: Verificar se a alíquota é válida
        if (request.getAliquota() == null || request.getAliquota() < 0) {
            throw new IllegalArgumentException("A alíquota deve ser um valor positivo.");
        }

        // Criação do objeto Imposto
        Imposto imposto = new Imposto();
        imposto.setNome(request.getNome());
        imposto.setDescricao(request.getDescricao());
        imposto.setAliquota(request.getAliquota());

        // Salvar no banco de dados
        Imposto salvo = impostoRepository.save(imposto);

        // Retornar a resposta convertida
        return convertToResponse(salvo);
    }

    // Buscar imposto por ID
    public ImpostoResponse buscarPorId(Long id) {
        Imposto imposto = impostoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imposto não encontrado com ID: " + id));
        return convertToResponse(imposto);
    }

    // Excluir imposto por ID
    public void excluir(Long id) {
        Imposto imposto = impostoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imposto não encontrado com ID: " + id));
        impostoRepository.deleteById(id);
    }

    // Calcular imposto com base no valor base
    public Map<String, Object> calcularImposto(Long id, Double valorBase) {
        // Validação do valor base
        if (valorBase == null || valorBase < 0) {
            throw new IllegalArgumentException("O valor base deve ser um valor positivo.");
        }

        // Busca o imposto pelo ID
        Imposto imposto = impostoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imposto não encontrado com ID: " + id));

        // Calcula o valor do imposto
        Double valorImposto = valorBase * (imposto.getAliquota() / 100);

        // Retornar um Map com informações adicionais
        Map<String, Object> response = new HashMap<>();
        response.put("valorImposto", valorImposto);
        response.put("tipoImpostoId", id);
        response.put("valorBase", valorBase);
        response.put("aliquota", imposto.getAliquota());
        response.put("nomeImposto", imposto.getNome());
        return response;
    }

    // Método auxiliar para converter Imposto para ImpostoResponse
    private ImpostoResponse convertToResponse(Imposto imposto) {
        return new ImpostoResponse(
                imposto.getId(),
                imposto.getNome(),
                imposto.getDescricao(),
                imposto.getAliquota()
        );
    }
}