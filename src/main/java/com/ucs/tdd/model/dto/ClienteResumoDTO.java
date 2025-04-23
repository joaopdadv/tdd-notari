package com.ucs.tdd.model.dto;

import com.ucs.tdd.model.PagamentoEntity;

import java.util.List;

public class ClienteResumoDTO {
    private String nome;
    private Float totalPago;
    private Float totalDevido;
    private List<PagamentosReumoDTO> pagamentosOrdenados;

    public ClienteResumoDTO(String nome, Float totalPago, Float totalDevido, List<PagamentosReumoDTO> pagamentosOrdenados) {
        this.nome = nome;
        this.totalPago = totalPago;
        this.totalDevido = totalDevido;
        this.pagamentosOrdenados = pagamentosOrdenados;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(Float totalPago) {
        this.totalPago = totalPago;
    }

    public Float getTotalDevido() {
        return totalDevido;
    }

    public void setTotalDevido(Float totalDevido) {
        this.totalDevido = totalDevido;
    }

    public List<PagamentosReumoDTO> getPagamentosOrdenados() {
        return pagamentosOrdenados;
    }

    public void setPagamentosOrdenados(List<PagamentosReumoDTO> pagamentosOrdenados) {
        this.pagamentosOrdenados = pagamentosOrdenados;
    }
}

