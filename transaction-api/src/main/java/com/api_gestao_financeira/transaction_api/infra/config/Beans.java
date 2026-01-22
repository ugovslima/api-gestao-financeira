package com.api_gestao_financeira.transaction_api.infra.config;

import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.application.usecase.BuscarTransacaoPorIdUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.CriarTransacaoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean
    public CriarTransacaoUseCase criarTransacaoUseCase(
            TransacaoGateway gateway
    ) {
        return new CriarTransacaoUseCase(gateway);
    }

    @Bean
    public BuscarTransacaoPorIdUseCase buscarTransacaoPorIdUseCase(
            TransacaoGateway transacaoGateway
    ) {
        return new BuscarTransacaoPorIdUseCase(transacaoGateway);
    }
}
