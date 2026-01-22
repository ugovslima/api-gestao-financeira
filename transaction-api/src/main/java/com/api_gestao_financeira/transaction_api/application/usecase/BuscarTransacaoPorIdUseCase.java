package com.api_gestao_financeira.transaction_api.application.usecase;

import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;

public class BuscarTransacaoPorIdUseCase {

    private final TransacaoGateway transacaoGateway;

    public BuscarTransacaoPorIdUseCase(TransacaoGateway transacaoGateway) {
        this.transacaoGateway = transacaoGateway;
    }

    public Transacao executar(Long id) {
        return transacaoGateway.buscarPorId(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Transação não encontrada para o id: " + id)
                );
    }
}
