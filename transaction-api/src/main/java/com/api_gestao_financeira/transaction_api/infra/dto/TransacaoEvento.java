package com.api_gestao_financeira.transaction_api.infra.dto;

import com.api_gestao_financeira.transaction_api.core.enums.Banco;
import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.enums.StatusTransacao;

import java.math.BigDecimal;

public record TransacaoEvento(
        Long id,
        Long usuarioId,
        FormaPagamento formaPagamento,
        BigDecimal valor,
        StatusTransacao status,
        Banco banco
) {
}