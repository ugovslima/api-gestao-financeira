package com.api_gestao_financeira.processor_worker.application.dto;

import java.math.BigDecimal;

public record SaldoLimite(
        BigDecimal saldo,
        BigDecimal limite
) {
}
