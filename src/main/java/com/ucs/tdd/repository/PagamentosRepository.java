package com.ucs.tdd.repository;

import com.ucs.tdd.model.PagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentosRepository extends JpaRepository<PagamentoEntity, Integer> {
}
