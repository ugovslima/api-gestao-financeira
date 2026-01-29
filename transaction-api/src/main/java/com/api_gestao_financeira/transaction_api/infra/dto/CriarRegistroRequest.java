package com.api_gestao_financeira.transaction_api.infra.dto;

import com.api_gestao_financeira.transaction_api.core.enums.Banco;
import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.enums.Moeda;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(
        description = """
        Dados para registro manual de transação.
        - Se 'parcelas' for null será considerado 1.
        - Se 'moeda' for null será considerado BRL.
        - 'data' deve estar no formato yyyy-MM-dd e não pode ser futura.
        """,
        example = """
    {
      "formaPagamento": "DINHEIRO",
      "valor": 35.90,
      "data": "2026-01-29",
      "descricao": "Supermecado",
      "parcelas": null,
      "banco": "ITAU",
      "moeda": null
    }
    """
)
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
