package com.api_gestao_financeira.processor_worker.application.usecase;

import com.api.gestaofinanceira.common.enums.FormaPagamento;
import com.api.gestaofinanceira.common.enums.StatusTransacao;
import com.api_gestao_financeira.processor_worker.application.dto.SaldoLimite;
import com.api_gestao_financeira.processor_worker.application.gateway.SaldoLimiteGateway;
import com.api_gestao_financeira.processor_worker.application.gateway.TransacaoRepository;
import com.api_gestao_financeira.processor_worker.core.domain.Transacao;

import java.math.BigDecimal;

public class ProcessarTransacaoUseCase {

    private final SaldoLimiteGateway saldoLimiteGateway;
    private final TransacaoRepository transacaoRepository;

    public ProcessarTransacaoUseCase(SaldoLimiteGateway saldoLimiteGateway, TransacaoRepository transacaoRepository) {
        this.saldoLimiteGateway = saldoLimiteGateway;
        this.transacaoRepository = transacaoRepository;
    }

    public void executar(Transacao transacao) {

        if (transacao.getStatus() != StatusTransacao.PENDENTE) {
            return;
        }

        if (transacao.getFormaPagamento() == FormaPagamento.DINHEIRO) {
            transacao.aprovarStatus();
            transacao.setMotivo("Transação em dinheiro - aprovada automaticamente");
            transacaoRepository.atualizar(transacao);
            return;
        }

        SaldoLimite saldoLimite =
                saldoLimiteGateway.consultarPorBanco(transacao.getBanco().name());

        if (transacao.getFormaPagamento() == FormaPagamento.CREDITO) {

            if (saldoLimite.limite().compareTo(transacao.getValor()) >= 0) {
                BigDecimal saldoRestante =
                        saldoLimite.limite().subtract(transacao.getValor());

                transacao.aprovarStatus();
                transacao.setMotivo(
                        "Limite disponível após compra: " + saldoRestante
                );
            } else {
                transacao.recusarStatus();
                transacao.setMotivo("Limite insuficiente. Limite atual: " + saldoLimite.limite());
            }

            transacaoRepository.atualizar(transacao);
            return;
        }

        if (transacao.getFormaPagamento() == FormaPagamento.DEBITO) {

            if (saldoLimite.saldo().compareTo(transacao.getValor()) >= 0) {
                BigDecimal saldoRestante =
                        saldoLimite.saldo().subtract(transacao.getValor());

                transacao.aprovarStatus();
                transacao.setMotivo(
                        "Saldo após compra: " + saldoRestante
                );
            } else {
                transacao.recusarStatus();
                transacao.setMotivo("Saldo insuficiente. Saldo atual: " + saldoLimite.saldo());
            }

            transacaoRepository.atualizar(transacao);
            return;
        }

        if (transacao.getFormaPagamento() == FormaPagamento.PIX) {

            if (saldoLimite.saldo().compareTo(transacao.getValor()) >= 0) {
                BigDecimal saldoRestante =
                        saldoLimite.saldo().subtract(transacao.getValor());

                transacao.aprovarStatus();
                transacao.setMotivo(
                        "Saldo após compra: " + saldoRestante
                );
            } else {
                transacao.recusarStatus();
                transacao.setMotivo("Saldo insuficiente. Saldo atual: " + saldoLimite.saldo());
            }

            transacaoRepository.atualizar(transacao);
        }
    }
}
