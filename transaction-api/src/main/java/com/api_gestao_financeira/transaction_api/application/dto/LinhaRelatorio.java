package com.api_gestao_financeira.transaction_api.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LinhaRelatorio(
        LocalDate data,
        String banco,
        String descricao,
        BigDecimal valor
) {
}
