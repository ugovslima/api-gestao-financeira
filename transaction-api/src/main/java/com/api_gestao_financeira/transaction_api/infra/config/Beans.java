package com.api_gestao_financeira.transaction_api.infra.config;

import com.api_gestao_financeira.transaction_api.application.gateway.CambioGateway;
import com.api_gestao_financeira.transaction_api.application.gateway.PublicarTransacaoGateway;
import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.application.usecase.BuscarTransacaoPorIdUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.CriarRegistroUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.CriarTransacaoPendenteUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.GerarRelatorioDespesasUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean
    public CriarTransacaoPendenteUseCase criarTransacaoUseCase(
            TransacaoGateway transacaoGateway,
            PublicarTransacaoGateway publicarTransacaoGateway,
            CambioGateway cambioGateway
    ) {
        return new CriarTransacaoPendenteUseCase(transacaoGateway, publicarTransacaoGateway, cambioGateway);
    }

    @Bean
    public BuscarTransacaoPorIdUseCase buscarTransacaoPorIdUseCase(
            TransacaoGateway transacaoGateway
    ) {
        return new BuscarTransacaoPorIdUseCase(transacaoGateway);
    }

    @Bean
    public CriarRegistroUseCase criarRegistroUseCase(
            TransacaoGateway transacaoGateway,
            CambioGateway cambioGateway
    ) {
        return new CriarRegistroUseCase(transacaoGateway, cambioGateway);
    }

    @Bean
    public GerarRelatorioDespesasUseCase gerarRelatorioDespesasUseCase(
            TransacaoGateway transacaoGateway
    ) {
        return new GerarRelatorioDespesasUseCase(transacaoGateway);
    }
}
