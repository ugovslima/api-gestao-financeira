package com.api_gestao_financeira.processor_worker.infra.config;

import com.api_gestao_financeira.processor_worker.application.gateway.SaldoLimiteGateway;
import com.api_gestao_financeira.processor_worker.application.gateway.TransacaoRepository;
import com.api_gestao_financeira.processor_worker.application.usecase.ProcessarTransacaoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Beans {

    @Bean
    public ProcessarTransacaoUseCase processarTransacaoUseCase(
            SaldoLimiteGateway saldoLimiteGateway,
            TransacaoRepository transacaoRepository
    ) {
        return new ProcessarTransacaoUseCase(
                saldoLimiteGateway,
                transacaoRepository
        );
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
