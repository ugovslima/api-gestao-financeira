package com.api_gestao_financeira.processor_worker.infra.gateway;

import com.api_gestao_financeira.processor_worker.application.dto.SaldoLimite;
import com.api_gestao_financeira.processor_worker.application.gateway.SaldoLimiteGateway;
import com.api_gestao_financeira.processor_worker.infra.dto.BancoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class SaldoLimiteGatewayImpl implements SaldoLimiteGateway {

    private static final String BASE_URL =
            "https://6973cb79b5f46f8b5828493e.mockapi.io/consulta-bancaria/";

    private final RestTemplate restTemplate;

    public SaldoLimiteGatewayImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public SaldoLimite consultarPorBanco(String banco, Long usuarioId) {

        String endpoint = BASE_URL + banco.toLowerCase();

        ResponseEntity<BancoResponse[]> response =
                restTemplate.getForEntity(endpoint, BancoResponse[].class);

        BancoResponse usuarioEncontrado =
                Arrays.stream(response.getBody())
                        .filter(u -> Long.valueOf(u.usuarioId()).equals(usuarioId))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException("Usuário não encontrado: " + usuarioId));

        return new SaldoLimite(
                usuarioEncontrado.saldo(),
                usuarioEncontrado.limite()
        );
    }
}