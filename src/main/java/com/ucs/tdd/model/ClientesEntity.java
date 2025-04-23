package com.ucs.tdd.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="Clientes")
public class ClientesEntity {

    @Id
    private Integer id;
    private String nome;
    private String dataNascimento;
    private String telefone;

    @OneToMany(mappedBy = "cliente")
    @JsonManagedReference
    private List<PagamentoEntity> pagamentos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<PagamentoEntity> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<PagamentoEntity> pagamentos) {
        this.pagamentos = pagamentos;
    }
}
