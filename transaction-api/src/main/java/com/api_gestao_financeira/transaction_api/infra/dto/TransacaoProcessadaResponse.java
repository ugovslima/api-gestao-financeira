package com.api_gestao_financeira.transaction_api.infra.dto;

import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.enums.StatusTransacao;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Cambio;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(
        description = "Resposta de uma transação processada",
        example = """
    {
      "id": 1,
      "usuarioId": 1,
      "formaPagamento": "CREDITO",
      "valorBRL": 199.90,
      "status": "APROVADA",
      "motivo": "Limite disponível após compra: 59.90",
      "data": "2026-01-29",
      "descricao": "Compra no mercado",
      "parcelas": 1,
      "banco": "ITAU",
      "cambio": {
        "moeda": "BRL",
        "taxa": 0
      }
    }
    """
)
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
