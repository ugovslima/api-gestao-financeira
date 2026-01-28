package com.api_gestao_financeira.transaction_api.infra.dto;

import com.api.gestaofinanceira.common.enums.Banco;
import com.api.gestaofinanceira.common.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.enums.Moeda;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CriarTransacaoRequest(
        Long usuarioId,
        @NotNull(message = "Forma de pagamento é um campo obrigatorio")
        FormaPagamento formaPagamento,
        @NotNull(message = "Valor é um campo obrigatorio")
        @Positive(message = "Valor deve ser maior que 0")
        BigDecimal valor,
        @NotNull(message = "Descrição é um campo obrigatorio")
        String descricao,
        Integer parcelas,
        @NotNull(message = "Banco é um campo obrigatorio")
        Banco banco,
        Moeda moeda
) {}