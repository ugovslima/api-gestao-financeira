package com.api_gestao_financeira.transaction_api.infra.gateway;

import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api_gestao_financeira.transaction_api.infra.persistence.entity.TransacaoEntity;
import com.api_gestao_financeira.transaction_api.infra.persistence.mapper.TransacaoMapper;
import com.api_gestao_financeira.transaction_api.infra.persistence.repository.TransacaoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransacaoGatewayJpa implements TransacaoGateway {

    private final TransacaoRepository repository;

    public TransacaoGatewayJpa(TransacaoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transacao salvar(Transacao transacao) {
        TransacaoEntity entity = TransacaoMapper.toEntity(transacao);
        TransacaoEntity salva = repository.save(entity);
        return TransacaoMapper.toDomain(salva);
    }

    @Override
    public Optional<Transacao> buscarPorId(Long id) {
        return repository.findById(id)
                .map(TransacaoMapper::toDomain);
    }
}
