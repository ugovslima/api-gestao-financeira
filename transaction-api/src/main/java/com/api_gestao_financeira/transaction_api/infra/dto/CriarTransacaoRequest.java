package com.api_gestao_financeira.transaction_api.infra.dto;

import com.api_gestao_financeira.transaction_api.core.enums.Banco;
import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.enums.Moeda;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(
        description = "Dados para criação de transação. " +
                "Se 'parcelas' for null será considerado 1 parcela. " +
                "Se 'moeda' for null será considerado BRL.",
        example = """
    {
      "formaPagamento": "DEBITO",
      "valor": 1,
      "descricao": "Compra no supermercado",
      "parcelas": null,
      "banco": "SANTANDER",
      "moeda": "EUR" 
    }
    """
)
public record CriarTransacaoRequest(
        Long usuarioId,
        @NotNull
        FormaPagamento formaPagamento,
        @NotNull
        @Positive
        BigDecimal valor,
        @NotNull
        String descricao,
        Integer parcelas,
        @NotNull
        Banco banco,
        Moeda moeda
) {}