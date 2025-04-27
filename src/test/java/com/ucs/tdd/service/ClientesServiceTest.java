package com.ucs.tdd.service;

import com.ucs.tdd.model.ClientesEntity;
import com.ucs.tdd.model.PagamentoEntity;
import com.ucs.tdd.model.dto.ClienteResumoDTO;
import com.ucs.tdd.model.dto.PagamentosReumoDTO;
import com.ucs.tdd.repository.ClientesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientesServiceTest {

    @Mock
    private ClientesRepository clientesRepository;
    @InjectMocks
    private ClientesService service;

    private ClientesEntity c1, c2;
    private PagamentoEntity p1, p2;

    @BeforeEach
    void setup() {
        // cliente 1 com dois pagamentos: um 't' (pago) de 100, e um não-'t' (devido) de 50
        c1 = new ClientesEntity();
        c1.setId(1);
        c1.setNome("Alice");
        p1 = new PagamentoEntity();
        p1.setValor(100f);
        p1.setTipoPagamento('t');
        p2 = new PagamentoEntity();
        p2.setValor(50f);
        p2.setTipoPagamento('d');
        p1.setCliente(c1); p2.setCliente(c1);
        c1.setPagamentos(List.of(p1, p2));

        // cliente 2 sem pagamentos
        c2 = new ClientesEntity();
        c2.setId(2);
        c2.setNome("Bob");
        c2.setPagamentos(Collections.emptyList());
    }

    @Test
    void testGetClients_delegaAoRepository() {
        when(clientesRepository.findAll()).thenReturn(List.of(c1, c2));
        var todos = service.getClients();
        assertThat(todos).hasSize(2).containsExactly(c1, c2);
        verify(clientesRepository, times(1)).findAll();
    }

    @Test
    void testGetDevidosDeTodos() {
        when(clientesRepository.findAll()).thenReturn(List.of(c1, c2));
        Map<String, Float> devidos = service.getDevidosDeTodos();
        assertThat(devidos)
                .containsEntry("Alice", 50f)
                .containsEntry("Bob", 0f);
    }

    @Test
    void testGetPagoDeTodos() {
        when(clientesRepository.findAll()).thenReturn(List.of(c1, c2));
        Map<String, Float> pagos = service.getPagoDeTodos();
        assertThat(pagos)
                .containsEntry("Alice", 100f)
                .containsEntry("Bob", 0f);
    }

    @Test
    void testGetResumoClientes() {
        // para forçar parseData, simulamos datas com  DDMMYYYY
        p1.setDataPagamento("01012021"); // 1 Jan 2021
        p2.setDataPagamento("02012021"); // 2 Jan 2021

        when(clientesRepository.findAll()).thenReturn(List.of(c1));
        List<ClienteResumoDTO> resumo = service.getResumoClientes();

        assertThat(resumo).hasSize(1);
        ClienteResumoDTO dto = resumo.getFirst();
        assertThat(dto.getNome()).isEqualTo("Alice");
        // total pago
        assertThat(dto.getTotalPago()).isEqualTo(100f);
        // total devido
        assertThat(dto.getTotalDevido()).isEqualTo(50f);

        List<PagamentosReumoDTO> detalhes = dto.getPagamentosOrdenados();
        assertThat(detalhes).extracting(PagamentosReumoDTO::getDataPagamento)
                .containsExactly(LocalDate.of(2021,1,1), LocalDate.of(2021,1,2));
    }
}
