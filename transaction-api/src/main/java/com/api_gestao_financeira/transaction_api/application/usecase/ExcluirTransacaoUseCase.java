package com.api_gestao_financeira.transaction_api.application.usecase;

import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;

public class ExcluirTransacaoUseCase {

    private final TransacaoGateway transacaoGateway;

    public ExcluirTransacaoUseCase(TransacaoGateway transacaoGateway) {
        this.transacaoGateway = transacaoGateway;
    }

    public void executar(Long transacaoId) {
        transacaoGateway.excluirPorId(transacaoId);
    }
}
