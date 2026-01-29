package com.api_gestao_financeira.transaction_api.infra.dto;

import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.enums.StatusTransacao;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Cambio;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(
        description = "Resposta de uma transação",
        example = """
    {
      "id": 1,
      "usuarioId": 1,
      "formaPagamento": "DEBITO",
      "valorBRL": 25.80,
      "status": "REGISTRADA",
      "data": "2026-01-29",
      "descricao": "Pagamento de assinatura",
      "parcelas": 1,
      "banco": "SICREDI",
      "cambio": {
        "moeda": "BRL",
        "taxa": 0
      }
    }
    """
)
public record TransacaoResponse(
        Long id,
        Long usuarioId,
        FormaPagamento formaPagamento,
        BigDecimal valorBRL,
        StatusTransacao status,
        LocalDate data,
        String descricao,
        Integer parcelas,
        String banco,
        Cambio cambio
) {
}