package com.api_gestao_financeira.transaction_api.infra.config;

import com.api_gestao_financeira.transaction_api.application.gateway.CambioGateway;
import com.api_gestao_financeira.transaction_api.application.gateway.PublicarTransacaoGateway;
import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean
    public CriarTransacaoUseCase criarTransacaoUseCase(
            TransacaoGateway transacaoGateway,
            PublicarTransacaoGateway publicarTransacaoGateway,
            CambioGateway cambioGateway
    ) {
        return new CriarTransacaoUseCase(transacaoGateway, publicarTransacaoGateway, cambioGateway);
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

    @Bean
    public GerarRelatorioDespesasPlanilhaUseCase gerarRelatorioDespesasPlanilhaUseCase(
            TransacaoGateway transacaoGateway
    ) {
        return new GerarRelatorioDespesasPlanilhaUseCase(transacaoGateway);
    }

    @Bean
    public BuscarTransacoesPorUsuarioIdUseCase buscarTransacoesDoUsuarioUseCase(
            TransacaoGateway transacaoGateway) {
        return new BuscarTransacoesPorUsuarioIdUseCase(transacaoGateway);
    }

    @Bean
    public ExcluirTransacaoUseCase excluirTransacaoUseCase(
            TransacaoGateway transacaoGateway) {
        return new ExcluirTransacaoUseCase(transacaoGateway);
    }
}
