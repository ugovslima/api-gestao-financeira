package com.api_gestao_financeira.processor_worker.infra.dto;

import java.math.BigDecimal;

public record BancoResponse(
        String nome,
        BigDecimal saldo,
        BigDecimal limite
) {
}
