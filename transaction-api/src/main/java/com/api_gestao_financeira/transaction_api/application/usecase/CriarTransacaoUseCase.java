package com.api_gestao_financeira.transaction_api.application.usecase;

import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Banco;
import com.api_gestao_financeira.transaction_api.core.valueObjects.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Parcelas;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CriarTransacaoUseCase {

    private final TransacaoGateway transacaoGateway;

    public CriarTransacaoUseCase(TransacaoGateway transacaoGateway) {
        this.transacaoGateway = transacaoGateway;
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

        return transacaoGateway.salvar(transacao);
    }
}
