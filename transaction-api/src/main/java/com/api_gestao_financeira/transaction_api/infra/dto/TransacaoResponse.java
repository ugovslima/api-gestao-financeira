package com.api_gestao_financeira.transaction_api.infra.dto;

import com.api.gestaofinanceira.common.enums.FormaPagamento;
import com.api.gestaofinanceira.common.enums.StatusTransacao;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Cambio;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoResponse(
        Long id,
        Long usuarioId,
        FormaPagamento formaPagamento,
        BigDecimal valor,
        StatusTransacao status,
        LocalDate data,
        String descricao,
        Integer parcelas,
        String banco,
        Cambio cambio
) {
}