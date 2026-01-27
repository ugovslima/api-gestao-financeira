package com.api_gestao_financeira.transaction_api.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record RelatorioDespesasPlanilha(
        String periodo,

        List<LinhaRelatorio> debito,
        List<LinhaRelatorio> credito,
        List<LinhaRelatorio> pix,
        List<LinhaRelatorio> dinheiro,

        BigDecimal totalDebito,
        BigDecimal totalCredito,
        BigDecimal totalPix,
        BigDecimal totalDinheiro,

        BigDecimal totalGeral
) {
}
