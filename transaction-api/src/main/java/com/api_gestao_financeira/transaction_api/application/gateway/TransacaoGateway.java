package com.api_gestao_financeira.transaction_api.application.gateway;

import com.api_gestao_financeira.transaction_api.core.domain.Transacao;

import java.util.Optional;

public interface TransacaoGateway {

    Transacao salvar(Transacao transacao);
    Optional<Transacao> buscarPorId(Long id);
}
