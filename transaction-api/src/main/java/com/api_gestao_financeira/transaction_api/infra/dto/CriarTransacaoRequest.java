package com.api_gestao_financeira.transaction_api.infra.dto;

import com.api.gestaofinanceira.common.enums.Banco;
import com.api.gestaofinanceira.common.enums.FormaPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CriarTransacaoRequest(
        Long usuarioId,
        FormaPagamento formaPagamento,
        BigDecimal valor,
        LocalDate data,
        String descricao,
        Integer parcelas,
        Banco banco
) {}