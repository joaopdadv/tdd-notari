package com.ucs.tdd.model.dto;

import java.time.LocalDate;

public class PagamentosReumoDTO {

    private LocalDate dataPagamento;
    private Float quantidade;
    private Float valor;
    private Character tipoPagamento;

    public PagamentosReumoDTO(LocalDate dataPagamento, Float quantidade, Float valor, Character tipoPagamento) {
        this.dataPagamento = dataPagamento;
        this.quantidade = quantidade;
        this.valor = valor;
        this.tipoPagamento = tipoPagamento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float quantidade) {
        this.quantidade = quantidade;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Character getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(Character tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }
}
