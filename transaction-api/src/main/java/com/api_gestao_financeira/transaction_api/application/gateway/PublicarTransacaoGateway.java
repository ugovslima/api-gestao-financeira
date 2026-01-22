package com.api_gestao_financeira.transaction_api.application.gateway;

import com.api_gestao_financeira.transaction_api.core.domain.Transacao;

public interface PublicarTransacaoGateway {
    void publicarTransacao(Transacao transacao);
}
