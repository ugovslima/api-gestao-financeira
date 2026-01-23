package com.api_gestao_financeira.transaction_api.application.usecase;

import com.api_gestao_financeira.transaction_api.application.gateway.PublicarTransacaoGateway;
import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api.gestaofinanceira.common.enums.Banco;
import com.api.gestaofinanceira.common.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Parcelas;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CriarTransacaoUseCase {

    private final TransacaoGateway transacaoGateway;
    private final PublicarTransacaoGateway publicarTransacaoGateway;

    public CriarTransacaoUseCase(TransacaoGateway transacaoGateway, PublicarTransacaoGateway publicarTransacaoGateway) {
        this.transacaoGateway = transacaoGateway;
        this.publicarTransacaoGateway = publicarTransacaoGateway;
    }

    public Transacao executar(
            Long usuarioId,
            FormaPagamento formaPagamento,
            BigDecimal valor,
            LocalDate data,
            String descricao,
            Integer parcelas,
            Banco banco
    ) {

        Transacao transacao = new Transacao(
                usuarioId,
                formaPagamento,
                valor,
                data,
                descricao,
                Parcelas.criar(formaPagamento, parcelas),
                banco
        );

        Transacao salva = transacaoGateway.salvar(transacao);
        publicarTransacaoGateway.publicarTransacao(salva);

        return salva;
    }
}
