package com.api_gestao_financeira.transaction_api.infra.dto;

import java.util.List;

public record CambioBrasilApiResponse(
        List<CotacaoResponse> cotacoes,
        String moeda,
        String data
) {
}
