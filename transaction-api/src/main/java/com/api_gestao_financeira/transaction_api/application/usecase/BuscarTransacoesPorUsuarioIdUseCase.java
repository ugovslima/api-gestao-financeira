package com.api_gestao_financeira.transaction_api.application.usecase;

import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;

import java.util.List;

public class BuscarTransacoesPorUsuarioIdUseCase {

    private final TransacaoGateway transacaoGateway;

    public BuscarTransacoesPorUsuarioIdUseCase(TransacaoGateway transacaoGateway) {
        this.transacaoGateway = transacaoGateway;
    }

    public List<Transacao> executar(Long usuarioId) {
        return transacaoGateway.buscarTransacoesPorUsuarioId(usuarioId);
    }
}
