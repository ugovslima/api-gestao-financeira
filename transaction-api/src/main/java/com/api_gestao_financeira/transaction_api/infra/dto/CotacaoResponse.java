package com.api_gestao_financeira.transaction_api.infra.dto;

import java.math.BigDecimal;

public record CotacaoResponse(
        BigDecimal cotacao_compra,
        BigDecimal cotacao_venda,
        String tipo_boletim,
        String data_hora_cotacao
) {
}
