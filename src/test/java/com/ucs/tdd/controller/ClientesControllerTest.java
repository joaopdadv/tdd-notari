package com.ucs.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucs.tdd.model.ClientesEntity;
import com.ucs.tdd.model.dto.ClienteResumoDTO;
import com.ucs.tdd.service.ClientesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ClientesController.class)
class ClientesControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private ClientesService service;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void readFiles_retornaOkEChamaService() throws Exception {
        mvc.perform(get("/clientes/read"))
                .andExpect(status().isOk());
        verify(service, times(1)).readFiles();
    }

    @Test
    void getClients_retornaLista() throws Exception {
        ClientesEntity c = new ClientesEntity();
        c.setId(1); c.setNome("X");
        when(service.getClients()).thenReturn(List.of(c));

        mvc.perform(get("/clientes/getAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("X"));
    }

    @Test
    void getValorDevido_retornaMapa() throws Exception {
        Map<String, Float> m = Map.of("A", 10f);
        when(service.getDevidosDeTodos()).thenReturn(m);

        mvc.perform(get("/clientes/valorDevido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.A").value(10.0));
    }

    @Test
    void getValorPago_retornaMapa() throws Exception {
        Map<String, Float> m = Map.of("B", 5f);
        when(service.getPagoDeTodos()).thenReturn(m);

        mvc.perform(get("/clientes/valorPago"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.B").value(5.0));
    }

    @Test
    void getResumo_retornaDTO() throws Exception {
        ClienteResumoDTO dto = new ClienteResumoDTO(
                "Z", 1f, 2f, Collections.emptyList()
        );
        when(service.getResumoClientes()).thenReturn(List.of(dto));

        mvc.perform(get("/clientes/resumo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Z"))
                .andExpect(jsonPath("$[0].totalPago").value(1.0))
                .andExpect(jsonPath("$[0].totalDevido").value(2.0));
    }
}