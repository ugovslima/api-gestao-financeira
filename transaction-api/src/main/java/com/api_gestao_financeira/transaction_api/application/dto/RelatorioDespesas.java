package com.api_gestao_financeira.transaction_api.application.dto;

import java.math.BigDecimal;

public record RelatorioDespesas(
        String periodo,
        Long usuarioId,
        BigDecimal valorTotal,
        int totalTransacoes,
        BigDecimal ticketMedio,
        BigDecimal maiorGasto
) {
}