package com.api_gestao_financeira.processor_worker.infra.mapper;

import com.api.gestaofinanceira.common.enums.Banco;
import com.api.gestaofinanceira.common.enums.FormaPagamento;
import com.api.gestaofinanceira.common.enums.StatusTransacao;
import com.api_gestao_financeira.processor_worker.core.domain.Transacao;
import com.api_gestao_financeira.processor_worker.infra.dto.TransacaoEvento;
import com.api_gestao_financeira.processor_worker.infra.persistence.entity.TransacaoEntity;

public class TransacaoMapper {

    private TransacaoMapper() {}

    public static Transacao toDomain(TransacaoEvento evento) {

        return new Transacao(
                evento.id(),
                evento.usuarioId(),
                FormaPagamento.valueOf(evento.formaPagamento()),
                evento.valor(),
                StatusTransacao.valueOf(evento.status()),
                Banco.valueOf(evento.banco())
        );
    }

    public static TransacaoEntity toEntity(Transacao transacao) {
        TransacaoEntity entity = new TransacaoEntity();
        entity.setId(transacao.getId());
        entity.setUsuarioId(transacao.getUsuarioId());
        entity.setFormaPagamento(transacao.getFormaPagamento());
        entity.setValor(transacao.getValor());
        entity.setBanco(transacao.getBanco());
        entity.setStatus(transacao.getStatus());
        entity.setMotivo(transacao.getMotivo());
        return entity;
    }


}
