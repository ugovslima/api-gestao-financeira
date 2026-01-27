package com.api_gestao_financeira.transaction_api.infra.dto;

import com.api.gestaofinanceira.common.enums.FormaPagamento;
import com.api.gestaofinanceira.common.enums.StatusTransacao;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Cambio;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoProcessadaResponse(
        Long id,
        Long usuarioId,
        FormaPagamento formaPagamento,
        BigDecimal valorBRL,
        StatusTransacao status,
        String motivo,
        LocalDate data,
        String descricao,
        Integer parcelas,
        String banco,
        Cambio cambio
) {
}
