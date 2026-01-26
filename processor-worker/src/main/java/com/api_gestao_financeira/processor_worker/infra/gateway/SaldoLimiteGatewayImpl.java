package com.api_gestao_financeira.processor_worker.infra.gateway;

import com.api_gestao_financeira.processor_worker.application.dto.SaldoLimite;
import com.api_gestao_financeira.processor_worker.application.gateway.SaldoLimiteGateway;
import com.api_gestao_financeira.processor_worker.infra.dto.BancoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class SaldoLimiteGatewayImpl implements SaldoLimiteGateway {

    private static final String URL =
            "https://6973cb79b5f46f8b5828493e.mockapi.io/consulta-bancaria/ugo";

    private final RestTemplate restTemplate;

    public SaldoLimiteGatewayImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public SaldoLimite consultarPorBanco(String banco) {

        ResponseEntity<BancoResponse[]> response =
                restTemplate.getForEntity(URL, BancoResponse[].class);

        BancoResponse bancoEncontrado =
                Arrays.stream(response.getBody())
                        .filter(b -> b.nome().equalsIgnoreCase(banco))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Banco não encontrado: " + banco));

        return new SaldoLimite(
                bancoEncontrado.saldo(),
                bancoEncontrado.limite()
        );
    }
}
