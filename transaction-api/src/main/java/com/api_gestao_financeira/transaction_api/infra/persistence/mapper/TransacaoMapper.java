package com.api_gestao_financeira.transaction_api.infra.persistence.mapper;

import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Parcelas;
import com.api_gestao_financeira.transaction_api.infra.dto.TransacaoEvento;
import com.api_gestao_financeira.transaction_api.infra.dto.TransacaoResponse;
import com.api_gestao_financeira.transaction_api.infra.persistence.entity.TransacaoEntity;

public class TransacaoMapper {

    public static TransacaoEntity toEntity(Transacao transacao){
        TransacaoEntity entity = new TransacaoEntity();
        entity.setUsuarioId(transacao.getUsuarioId());
        entity.setFormaPagamento(transacao.getFormaPagamento());
        entity.setValor(transacao.getValor());
        entity.setData(transacao.getData());
        entity.setDescricao(transacao.getDescricao());
        entity.setParcelas(transacao.getParcelas().getQuantidade());
        entity.setBanco(transacao.getBanco());
        entity.setStatus(transacao.getStatus());
        return entity;
    }

    public static Transacao toDomain(TransacaoEntity entity){
        Transacao transacao = new Transacao(
                entity.getUsuarioId(),
                entity.getFormaPagamento(),
                entity.getValor(),
                entity.getData(),
                entity.getDescricao(),
                Parcelas.criar(entity.getFormaPagamento(), entity.getParcelas()),
                entity.getBanco()
        );
        transacao.atribuirId(entity.getId());
        return transacao;
    }

    public static TransacaoResponse toResponse(Transacao transacao) {
        return new TransacaoResponse(
                transacao.getId(),
                transacao.getUsuarioId(),
                transacao.getFormaPagamento(),
                transacao.getValor(),
                transacao.getStatus(),
                transacao.getData(),
                transacao.getDescricao(),
                transacao.getParcelas().getQuantidade(),
                transacao.getBanco().name()
        );
    }

    public static TransacaoEvento toEvent(Transacao transacao) {
        return new TransacaoEvento(
                transacao.getId(),
                transacao.getUsuarioId(),
                transacao.getFormaPagamento(),
                transacao.getValor(),
                transacao.getStatus(),
                transacao.getBanco()
        );
    }
}
