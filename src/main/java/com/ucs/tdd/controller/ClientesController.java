package com.ucs.tdd.controller;

import com.ucs.tdd.model.ClientesEntity;
import com.ucs.tdd.model.dto.ClienteResumoDTO;
import com.ucs.tdd.service.ClientesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
public class ClientesController {

    private final ClientesService clientesService;

    public ClientesController(ClientesService clientesService) {
        this.clientesService = clientesService;
    }

    @GetMapping("/read")
    public ResponseEntity<Void> readFiles(){
        clientesService.readFiles();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ClientesEntity>> getClients(){
        return ResponseEntity.ok().body(clientesService.getClients());
    }

    @GetMapping("/valorDevido")
    public ResponseEntity<Map<String, Float>> getValorDevido(){
        return ResponseEntity.ok().body(clientesService.getDevidosDeTodos());
    }

    @GetMapping("/valorPago")
    public ResponseEntity<Map<String, Float>> getValorPago(){
        return ResponseEntity.ok().body(clientesService.getPagoDeTodos());
    }

    @GetMapping("/resumo")
    public ResponseEntity<List<ClienteResumoDTO>> getResumo(){
        return ResponseEntity.ok().body(clientesService.getResumoClientes());
    }
}
