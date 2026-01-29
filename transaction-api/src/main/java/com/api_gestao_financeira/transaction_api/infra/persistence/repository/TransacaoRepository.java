package com.api_gestao_financeira.transaction_api.infra.persistence.repository;

import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.infra.persistence.entity.TransacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<TransacaoEntity, Long> {
    List<TransacaoEntity> findByUsuarioIdAndDataBetween(
            Long usuarioId,
            LocalDate inicio,
            LocalDate fim
    );

    List<TransacaoEntity> findByUsuarioIdAndDataBetweenAndFormaPagamento(
            Long usuarioId,
            LocalDate inicio,
            LocalDate fim,
            FormaPagamento formaPagamento
    );
}

