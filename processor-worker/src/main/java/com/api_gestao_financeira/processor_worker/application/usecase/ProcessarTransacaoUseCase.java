package com.api_gestao_financeira.processor_worker.application.usecase;

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

    public void processar(Transacao transacao){

        if(!transacao.getStatus().equals("PENDENTE")){
            return;
        }

        if ("DINHEIRO".equals(transacao.getFormaPagamento())) {

            transacao.aprovarStatus();
            transacao.setMotivo("Transação em dinheiro - aprovada automaticamente");
            transacaoRepository.atualizar(transacao);
            return;
        }

        SaldoLimite saldoLimite = saldoLimiteGateway.consultarPorBanco(transacao.getBanco());

        if ("CREDITO".equals(transacao.getFormaPagamento())
                && saldoLimite.limite().compareTo(transacao.getValor()) >= 0) {

            BigDecimal saldoRestante =
                    saldoLimite.saldo().subtract(transacao.getValor());

            transacao.aprovarStatus();
            transacao.setMotivo("Compra aprovada. Limite disponível após compra: " + saldoRestante);
            transacaoRepository.atualizar(transacao);
            return;

        } else if ("CREDITO".equals(transacao.getFormaPagamento())
                && saldoLimite.limite().compareTo(transacao.getValor()) < 0) {

            transacao.recusarStatus();
            transacao.setMotivo("Compra recusada. Limite insuficiente");
            transacaoRepository.atualizar(transacao);
            return;
        }

        if ("DEBITO".equals(transacao.getFormaPagamento())
                && saldoLimite.saldo().compareTo(transacao.getValor()) >= 0) {

            BigDecimal saldoRestante =
                    saldoLimite.saldo().subtract(transacao.getValor());

            transacao.aprovarStatus();
            transacao.setMotivo("Compra aprovada. Saldo após compra: " + saldoRestante);
            transacaoRepository.atualizar(transacao);
            return;

        } else if ("DEBITO".equals(transacao.getFormaPagamento())
                && saldoLimite.saldo().compareTo(transacao.getValor()) < 0) {

            transacao.recusarStatus();
            transacao.setMotivo("Compra recusada. Saldo insuficiente");
            transacaoRepository.atualizar(transacao);
            return;
        }

        if ("PIX".equals(transacao.getFormaPagamento())
                && saldoLimite.saldo().compareTo(transacao.getValor()) >= 0) {

            BigDecimal saldoRestante =
                    saldoLimite.saldo().subtract(transacao.getValor());

            transacao.aprovarStatus();
            transacao.setMotivo("Compra aprovada via PIX. Saldo após compra: " + saldoRestante);
            transacaoRepository.atualizar(transacao);
            return;

        } else if ("PIX".equals(transacao.getFormaPagamento())
                && saldoLimite.saldo().compareTo(transacao.getValor()) < 0) {

            transacao.recusarStatus();
            transacao.setMotivo("Compra recusada. Saldo insuficiente para PIX");
            transacaoRepository.atualizar(transacao);
            return;
        }
    }
}
