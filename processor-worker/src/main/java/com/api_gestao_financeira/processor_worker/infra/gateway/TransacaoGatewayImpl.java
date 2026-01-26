package com.api_gestao_financeira.processor_worker.infra.gateway;

import com.api_gestao_financeira.processor_worker.application.gateway.TransacaoRepository;
import com.api_gestao_financeira.processor_worker.core.domain.Transacao;
import com.api_gestao_financeira.processor_worker.infra.persistence.repository.TransacaoRepositoryJpa;
import org.springframework.stereotype.Component;

@Component
public class TransacaoGatewayImpl implements TransacaoRepository {

    private final TransacaoRepositoryJpa transacaoRepositoryJpa;

    public TransacaoGatewayImpl(TransacaoRepositoryJpa transacaoRepositoryJpa) {
        this.transacaoRepositoryJpa = transacaoRepositoryJpa;
    }

    @Override
    public void atualizar(Transacao transacao) {
        transacaoRepositoryJpa.atualizarStatus(
                transacao.getId(),
                transacao.getStatus(),
                transacao.getMotivo()
        );
    }
}
