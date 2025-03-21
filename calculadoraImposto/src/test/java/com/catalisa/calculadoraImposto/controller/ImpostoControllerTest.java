package com.catalisa.calculadoraImposto.controller;

import com.catalisa.calculadoraImposto.dto.ImpostoResponse;
import com.catalisa.calculadoraImposto.service.ImpostoService;
import com.catalisa.calculadoraImposto.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ImpostoController.class)
class ImpostoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImpostoService impostoService;

    @MockBean
    private JwtUtil jwtUtil;

    private ImpostoResponse impostoResponse;

    @BeforeEach
    void setUp() {
        impostoResponse = new ImpostoResponse(1L, "ICMS", "Imposto sobre Circulação de Mercadorias e Serviços", 18.0);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listarTodos_DeveRetornarListaDeImpostos() throws Exception {
        List<ImpostoResponse> impostos = Arrays.asList(impostoResponse);
        Mockito.when(impostoService.listarTodos()).thenReturn(impostos);

        mockMvc.perform(get("/tipos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nome", is("ICMS")))
                .andExpect(jsonPath("$[0].descricao", is("Imposto sobre Circulação de Mercadorias e Serviços")))
                .andExpect(jsonPath("$[0].aliquota", is(18.0)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cadastrar_DeveRetornarImpostoCriado() throws Exception {
        mockMvc.perform(post("/tipos")
                        .with(csrf()) // Adiciona o token CSRF automaticamente
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"IPI\",\"descricao\":\"Imposto sobre Produtos Industrializados\",\"aliquota\":12.0}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void buscarPorId_DeveRetornarImposto() throws Exception {
        Mockito.when(impostoService.buscarPorId(anyLong())).thenReturn(impostoResponse);

        mockMvc.perform(get("/tipos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifica o status HTTP 200
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("ICMS")))
                .andExpect(jsonPath("$.descricao", is("Imposto sobre Circulação de Mercadorias e Serviços")))
                .andExpect(jsonPath("$.aliquota", is(18.0)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void excluir_DeveRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/tipos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void calcularImposto_DeveRetornarValorCalculado() throws Exception {
        // Mock do serviço
        Mockito.when(impostoService.calcularImposto(anyLong(), any(Double.class)))
                .thenReturn(Map.of(
                        "valorImposto", 180.0,
                        "tipoImpostoId", 1L,
                        "valorBase", 1000.0,
                        "aliquota", 18.0,
                        "nomeImposto", "ICMS"
                ));

        mockMvc.perform(post("/tipos/calculo")
                        .with(csrf()) // Adiciona o token CSRF automaticamente
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipoImpostoId\":1,\"valorBase\":1000.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorImposto", is(180.0)))
                .andExpect(jsonPath("$.valorBase", is(1000.0)))
                .andExpect(jsonPath("$.tipoImpostoId", is(1)))
                .andExpect(jsonPath("$.aliquota", is(18.0)))
                .andExpect(jsonPath("$.nomeImposto", is("ICMS")));
        }
    }