package com.api_gestao_financeira.transaction_api.infra.dto;

import com.api_gestao_financeira.transaction_api.core.enums.Banco;
import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.enums.Moeda;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CriarRegistroRequest(
        Long usuarioId,
        @NotNull
        FormaPagamento formaPagamento,
        @NotNull
        @Positive
        BigDecimal valor,
        @NotNull
        @PastOrPresent
        LocalDate data,
        @NotNull
        String descricao,
        Integer parcelas,
        @NotNull
        Banco banco,
        Moeda moeda
) {
}
