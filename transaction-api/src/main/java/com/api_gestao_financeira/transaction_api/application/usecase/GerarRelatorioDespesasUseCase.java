package com.api_gestao_financeira.transaction_api.application.usecase;

import com.api.gestaofinanceira.common.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.application.dto.RelatorioDespesas;
import com.api_gestao_financeira.transaction_api.application.dto.TipoPeriodo;
import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class GerarRelatorioDespesasUseCase {

    private final TransacaoGateway transacaoGateway;

    public GerarRelatorioDespesasUseCase(TransacaoGateway transacaoGateway) {
        this.transacaoGateway = transacaoGateway;
    }

    public RelatorioDespesas executar(
            Long usuarioId,
            LocalDate dataReferencia,
            TipoPeriodo tipoPeriodo,
            FormaPagamento formaPagamento
    ) {

        LocalDate inicio;
        LocalDate fim;

        if (tipoPeriodo == TipoPeriodo.DIARIO) {
            inicio = dataReferencia;
            fim = dataReferencia;

        } else if (tipoPeriodo == TipoPeriodo.MENSAL) {
            YearMonth ym = YearMonth.from(dataReferencia);
            inicio = ym.atDay(1);
            fim = ym.atEndOfMonth();

        } else {
            throw new IllegalArgumentException(
                    "Periodo invalido");
        }

        List<Transacao> transacoes =
                transacaoGateway.buscarPorPeriodo(
                        usuarioId,
                        inicio,
                        fim,
                        formaPagamento
                );

        if (transacoes.isEmpty()) {
            return new RelatorioDespesas(
                    tipoPeriodo == TipoPeriodo.MENSAL
                            ? YearMonth.from(dataReferencia).toString()
                            : dataReferencia.toString(),
                    usuarioId,
                    BigDecimal.ZERO,
                    0,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );
        }

        BigDecimal valorTotal = transacoes.stream()
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalTransacoes = transacoes.size();

        BigDecimal ticketMedio = valorTotal
                .divide(BigDecimal.valueOf(totalTransacoes), 2, RoundingMode.HALF_UP);

        BigDecimal maiorGasto = transacoes.stream()
                .map(Transacao::getValor)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        String periodo =
                tipoPeriodo == TipoPeriodo.MENSAL
                        ? YearMonth.from(dataReferencia).toString()
                        : dataReferencia.toString();

        return new RelatorioDespesas(
                periodo,
                usuarioId,
                valorTotal,
                totalTransacoes,
                ticketMedio,
                maiorGasto
        );
    }
}