package com.ucs.tdd.service;

import com.ucs.tdd.model.ClientesEntity;
import com.ucs.tdd.model.PagamentoEntity;
import com.ucs.tdd.model.dto.ClienteResumoDTO;
import com.ucs.tdd.model.dto.PagamentosReumoDTO;
import com.ucs.tdd.repository.ClientesRepository;
import com.ucs.tdd.repository.PagamentosRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ClientesService {

    private final ClientesRepository clientesRepository;
    private final PagamentosRepository pagamentosRepository;

    public ClientesService(ClientesRepository clientesRepository, PagamentosRepository pagamentosRepository) {
        this.clientesRepository = clientesRepository;
        this.pagamentosRepository = pagamentosRepository;
    }

    public void readFiles() {
        try {
            clientesRepository.deleteAll();
            pagamentosRepository.deleteAll();

            Map<Integer, ClientesEntity> clientesMap = new HashMap<>();
            String basePath = Paths.get("").toAbsolutePath().toString();

            // === Lê clientes.txt ===
            try (BufferedReader reader = new BufferedReader(new FileReader(basePath + "/clientes.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split(";", -1);
                    if (parts.length < 5) continue;

                    ClientesEntity cliente = new ClientesEntity();
                    cliente.setId(Integer.parseInt(parts[0]));
                    cliente.setNome(parts[4]);
                    cliente.setDataNascimento(parts[2].isEmpty() ? null : parts[2]);
                    cliente.setTelefone(parts[3].isEmpty() ? null : parts[3]);

                    clientesMap.put(cliente.getId(), cliente);
                }
            }

            clientesRepository.saveAll(clientesMap.values());

            // === Lê pagamentos.txt ===
            try (BufferedReader reader = new BufferedReader(new FileReader(basePath + "/pagamentos.txt"))) {
                String line;
                List<PagamentoEntity> pagamentos = new ArrayList<>();

                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split(";", -1);
                    if (parts.length < 5) continue;

                    Integer clienteId = Integer.parseInt(parts[2]);
                    ClientesEntity cliente = clientesMap.get(clienteId);
                    if (cliente == null) continue;

                    PagamentoEntity pagamento = new PagamentoEntity();
                    pagamento.setCliente(cliente);
                    pagamento.setDataPagamento(parts[1]);
                    pagamento.setQuantidade(Float.parseFloat(parts[2]));
                    pagamento.setValor(Float.parseFloat(parts[3])); // valor não está no arquivo
                    pagamento.setTipoPagamento(parts[4].charAt(0));

                    pagamentos.add(pagamento);
                }

                pagamentosRepository.saveAll(pagamentos);
            }

            System.out.println("Importação concluída com sucesso.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao importar arquivos: " + e.getMessage());
        }
    }

    public List<ClientesEntity> getClients(){
        return clientesRepository.findAll();
    }

    public Map<String, Float> getDevidosDeTodos() {
        List<ClientesEntity> clientes = clientesRepository.findAll();

        Map<String, Float> result = new HashMap<>();
        for (ClientesEntity cliente : clientes) {
            float total = cliente.getPagamentos()
                    .stream()
                    .map(p -> p.getValor() != null && p.getTipoPagamento() != 't' ? p.getValor() : 0f)
                    .reduce(0f, Float::sum);
            result.put(cliente.getNome(), total);
        }

        return result;
    }

    public Map<String, Float> getPagoDeTodos() {
        List<ClientesEntity> clientes = clientesRepository.findAll();

        Map<String, Float> result = new HashMap<>();
        for (ClientesEntity cliente : clientes) {
            float total = cliente.getPagamentos()
                    .stream()
                    .map(p -> p.getValor() != null && p.getTipoPagamento() == 't' ? p.getValor() : 0f)
                    .reduce(0f, Float::sum);
            result.put(cliente.getNome(), total);
        }

        return result;
    }

    public List<ClienteResumoDTO> getResumoClientes() {
        List<ClientesEntity> clientes = clientesRepository.findAll();
        List<ClienteResumoDTO> resumoList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");

        for (ClientesEntity cliente : clientes) {
            List<PagamentoEntity> pagamentos = cliente.getPagamentos();

            List<PagamentosReumoDTO> pagamentosResumo = pagamentos.stream()
                    .map(p -> new PagamentosReumoDTO(
                            parseData(p.getDataPagamento(), formatter),
                            p.getQuantidade() != null ? p.getQuantidade().floatValue() : 0f,
                            p.getValor() != null ? p.getValor() : 0f,
                            p.getTipoPagamento()
                    ))
                    .sorted(Comparator.comparing(PagamentosReumoDTO::getDataPagamento))
                    .toList();

            float totalPago = pagamentosResumo.stream()
                    .filter(p -> p.getTipoPagamento() != null && p.getTipoPagamento() == 't')
                    .map(PagamentosReumoDTO::getValor)
                    .reduce(0f, Float::sum);

            float totalDevido = pagamentosResumo.stream()
                    .filter(p -> p.getTipoPagamento() != null && p.getTipoPagamento() != 't')
                    .map(PagamentosReumoDTO::getValor)
                    .reduce(0f, Float::sum);

            ClienteResumoDTO dto = new ClienteResumoDTO(
                    cliente.getNome(),
                    totalPago,
                    totalDevido,
                    pagamentosResumo
            );

            resumoList.add(dto);
        }

        return resumoList;
    }

    private LocalDate parseData(String data, DateTimeFormatter formatter) {
        try {
            // Completa com zero à esquerda até ter 8 dígitos
            if (data.length() < 8) {
                data = String.format("%08d", Integer.parseInt(data));
            }
            return LocalDate.parse(data, formatter);
        } catch (Exception e) {
            return null;
        }
    }


}
