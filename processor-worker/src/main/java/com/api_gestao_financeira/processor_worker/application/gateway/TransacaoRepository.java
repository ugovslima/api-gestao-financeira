package com.api_gestao_financeira.processor_worker.application.gateway;

import com.api_gestao_financeira.processor_worker.core.domain.Transacao;

public interface TransacaoRepository {
    void atualizar(Transacao transacao);
}
