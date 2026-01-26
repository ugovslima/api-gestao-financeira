package com.api_gestao_financeira.transaction_api.infra.persistence.mapper;

import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Cambio;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Parcelas;
import com.api_gestao_financeira.transaction_api.infra.dto.TransacaoEvento;
import com.api_gestao_financeira.transaction_api.infra.dto.TransacaoProcessadaResponse;
import com.api_gestao_financeira.transaction_api.infra.dto.TransacaoResponse;
import com.api_gestao_financeira.transaction_api.infra.persistence.entity.TransacaoEntity;

public class TransacaoMapper {

    public static TransacaoEntity toEntity(Transacao transacao) {
        TransacaoEntity entity = new TransacaoEntity();
        entity.setUsuarioId(transacao.getUsuarioId());
        entity.setFormaPagamento(transacao.getFormaPagamento());
        entity.setValor(transacao.getValor());
        entity.setData(transacao.getData());
        entity.setDescricao(transacao.getDescricao());
        entity.setParcelas(transacao.getParcelas().getQuantidade());
        entity.setBanco(transacao.getBanco());
        entity.setStatus(transacao.getStatus());
        entity.setMoedaOrigem(transacao.getCambio().getMoeda());
        entity.setTaxaCambio(transacao.getCambio().getTaxa());
        return entity;
    }

    public static Transacao toDomain(TransacaoEntity entity) {
        Cambio cambio = Cambio.criar(
                entity.getMoedaOrigem(),
                entity.getTaxaCambio()
        );
        Transacao transacao = new Transacao(
                entity.getUsuarioId(),
                entity.getFormaPagamento(),
                entity.getValor(),
                entity.getData(),
                entity.getDescricao(),
                Parcelas.criar(entity.getFormaPagamento(), entity.getParcelas()),
                entity.getBanco(),
                cambio
        );
        transacao.atribuirId(entity.getId());
        transacao.atribuirStatus(entity.getStatus());
        transacao.atribuirMotivo(entity.getMotivo());

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
                transacao.getBanco().name(),
                transacao.getCambio()
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

    public static TransacaoProcessadaResponse toProcessadaResponse(Transacao transacao) {
        return new TransacaoProcessadaResponse(
                transacao.getId(),
                transacao.getUsuarioId(),
                transacao.getFormaPagamento(),
                transacao.getValor(),
                transacao.getStatus(),
                transacao.getMotivo(),
                transacao.getData(),
                transacao.getDescricao(),
                transacao.getParcelas().getQuantidade(),
                transacao.getBanco().name(),
                transacao.getCambio()
        );
    }
}
