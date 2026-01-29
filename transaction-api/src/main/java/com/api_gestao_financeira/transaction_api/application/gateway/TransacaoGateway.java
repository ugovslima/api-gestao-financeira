package com.api_gestao_financeira.transaction_api.application.gateway;

import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransacaoGateway {

    Transacao salvar(Transacao transacao);
    Optional<Transacao> buscarPorId(Long id);
    List<Transacao> buscarPorPeriodo(
            Long usuarioId,
            LocalDate inicio,
            LocalDate fim,
            FormaPagamento formaPagamento
    );
}
