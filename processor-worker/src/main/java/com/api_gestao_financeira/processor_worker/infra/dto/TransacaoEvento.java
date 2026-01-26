package com.api_gestao_financeira.processor_worker.infra.dto;

import java.math.BigDecimal;

public record TransacaoEvento(
        Long id,
        Long usuarioId,
        String formaPagamento,
        BigDecimal valor,
        String status,
        String banco
) {
}
