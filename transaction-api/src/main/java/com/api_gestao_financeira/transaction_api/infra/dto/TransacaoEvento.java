package com.api_gestao_financeira.transaction_api.infra.dto;

import com.api_gestao_financeira.transaction_api.core.valueObjects.Banco;
import com.api_gestao_financeira.transaction_api.core.valueObjects.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.valueObjects.StatusTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoEvento(
        Long id,
        Long usuarioId,
        FormaPagamento formaPagamento,
        BigDecimal valor,
        StatusTransacao status,
        Banco banco
) {
}