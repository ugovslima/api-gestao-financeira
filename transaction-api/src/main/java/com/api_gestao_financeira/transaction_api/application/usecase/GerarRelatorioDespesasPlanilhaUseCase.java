package com.api_gestao_financeira.transaction_api.application.usecase;

import com.api.gestaofinanceira.common.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.application.dto.LinhaRelatorio;
import com.api_gestao_financeira.transaction_api.application.dto.RelatorioDespesasPlanilha;
import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class GerarRelatorioDespesasPlanilhaUseCase {

    private final TransacaoGateway transacaoGateway;

    public GerarRelatorioDespesasPlanilhaUseCase(
            TransacaoGateway transacaoGateway
    ) {
        this.transacaoGateway = transacaoGateway;
    }

    public RelatorioDespesasPlanilha executar(
            Long usuarioId,
            LocalDate dataReferencia
    ) {
        YearMonth ym = YearMonth.from(dataReferencia);
        LocalDate inicio = ym.atDay(1);
        LocalDate fim = ym.atEndOfMonth();

        List<Transacao> transacoes =
                transacaoGateway.buscarPorPeriodo(
                                usuarioId,
                                inicio,
                                fim,
                                null
                        )
                        .stream()
                        .filter(Transacao::validaParaRelatorio)
                        .toList();

        if (transacoes.isEmpty()) {
            throw new IllegalArgumentException("Esse usuário não tem despesas no mês escolhido.");
        }

        List<Transacao> debito = filtrar(transacoes, FormaPagamento.DEBITO);
        List<Transacao> credito = filtrar(transacoes, FormaPagamento.CREDITO);
        List<Transacao> pix = filtrar(transacoes, FormaPagamento.PIX);
        List<Transacao> dinheiro = filtrar(transacoes, FormaPagamento.DINHEIRO);

        BigDecimal totalDebito = somar(debito);
        BigDecimal totalCredito = somar(credito);
        BigDecimal totalPix = somar(pix);
        BigDecimal totalDinheiro = somar(dinheiro);

        BigDecimal totalGeral = totalDebito
                .add(totalCredito)
                .add(totalPix)
                .add(totalDinheiro);

        String periodo = ym.toString();

        return new RelatorioDespesasPlanilha(
                periodo,
                mapear(debito),
                mapear(credito),
                mapear(pix),
                mapear(dinheiro),
                totalDebito,
                totalCredito,
                totalPix,
                totalDinheiro,
                totalGeral
        );
    }

    private List<Transacao> filtrar(
            List<Transacao> transacoes,
            FormaPagamento forma
    ) {
        return transacoes.stream()
                .filter(t -> t.getFormaPagamento() == forma)
                .toList();
    }

    private BigDecimal somar(List<Transacao> transacoes) {
        return transacoes.stream()
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<LinhaRelatorio> mapear(
            List<Transacao> transacoes
    ) {
        return transacoes.stream()
                .map(t -> new LinhaRelatorio(
                        t.getData(),
                        t.getBanco().toString(),
                        t.getDescricao(),
                        t.getValor()
                ))
                .toList();
    }
}
