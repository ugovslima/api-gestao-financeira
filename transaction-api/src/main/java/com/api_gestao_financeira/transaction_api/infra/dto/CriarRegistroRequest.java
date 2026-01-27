package com.api_gestao_financeira.transaction_api.infra.dto;

import com.api.gestaofinanceira.common.enums.Banco;
import com.api.gestaofinanceira.common.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.enums.Moeda;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CriarRegistroRequest(
        Long usuarioId,
        FormaPagamento formaPagamento,
        BigDecimal valor,
        LocalDate data,
        String descricao,
        Integer parcelas,
        Banco banco,
        Moeda moeda
) {
}
