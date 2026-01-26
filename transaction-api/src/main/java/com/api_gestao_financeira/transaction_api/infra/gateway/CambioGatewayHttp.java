package com.api_gestao_financeira.transaction_api.infra.gateway;

import com.api_gestao_financeira.transaction_api.application.gateway.CambioGateway;
import com.api_gestao_financeira.transaction_api.core.enums.Moeda;
import com.api_gestao_financeira.transaction_api.infra.dto.CambioBrasilApiResponse;
import com.api_gestao_financeira.transaction_api.infra.dto.CotacaoResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class CambioGatewayHttp implements CambioGateway {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public BigDecimal buscarTaxa(Moeda moeda, LocalDate data) {

        if (moeda == Moeda.BRL) {
            return BigDecimal.ONE;
        }

        String url = String.format(
                "https://brasilapi.com.br/api/cambio/v1/cotacao/%s/%s",
                moeda.name(),
                data
        );

        CambioBrasilApiResponse response =
                restTemplate.getForObject(url, CambioBrasilApiResponse.class);

        if (response == null || response.cotacoes() == null) {
            throw new RuntimeException("Erro ao obter cotação");
        }

        return response.cotacoes().stream()
                .filter(c -> "FECHAMENTO PTAX".equals(c.tipo_boletim()))
                .findFirst()
                .map(CotacaoResponse::cotacao_venda)
                .orElseGet(() ->
                        response.cotacoes()
                                .get(response.cotacoes().size() - 1)
                                .cotacao_venda()
                );
    }
}
