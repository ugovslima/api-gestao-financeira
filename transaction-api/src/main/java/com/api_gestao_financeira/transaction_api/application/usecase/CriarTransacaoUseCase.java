package com.api_gestao_financeira.transaction_api.application.usecase;

import com.api_gestao_financeira.transaction_api.application.gateway.CambioGateway;
import com.api_gestao_financeira.transaction_api.application.gateway.PublicarTransacaoGateway;
import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api_gestao_financeira.transaction_api.core.enums.Banco;
import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.enums.Moeda;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Cambio;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Parcelas;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CriarTransacaoUseCase {

    private final TransacaoGateway transacaoGateway;
    private final PublicarTransacaoGateway publicarTransacaoGateway;
    private final CambioGateway cambioGateway;

    public CriarTransacaoUseCase(TransacaoGateway transacaoGateway, PublicarTransacaoGateway publicarTransacaoGateway, CambioGateway cambioGateway) {
        this.transacaoGateway = transacaoGateway;
        this.publicarTransacaoGateway = publicarTransacaoGateway;
        this.cambioGateway = cambioGateway;
    }

    public Transacao executar(
            Long usuarioId,
            FormaPagamento formaPagamento,
            BigDecimal valor,
            LocalDate data,
            String descricao,
            Integer parcelas,
            Banco banco,
            Moeda moeda) {

        LocalDate dataTransacao = LocalDate.now();
        LocalDate dataCambio = LocalDate.now().minusDays(1);

        while (dataCambio.getDayOfWeek().getValue() >= 6) {
            dataCambio = dataCambio.minusDays(1);
        }

        BigDecimal taxa = cambioGateway.buscarTaxa(moeda == null ? Moeda.BRL : moeda, dataCambio);

        Cambio cambio = Cambio.criar(moeda, taxa);
        BigDecimal valorEmReais = valor.multiply(cambio.getTaxa());

        Transacao transacao;

        if (formaPagamento == FormaPagamento.DINHEIRO) {
            transacao = Transacao.registrar(
                    usuarioId,
                    formaPagamento,
                    valorEmReais,
                    dataTransacao,
                    descricao,
                    Parcelas.criar(formaPagamento, parcelas),
                    null,
                    cambio
            );

            return transacaoGateway.salvar(transacao);
        }

        transacao = Transacao.criarPendente(
                usuarioId,
                formaPagamento,
                valorEmReais,
                dataTransacao,
                descricao,
                Parcelas.criar(formaPagamento, parcelas),
                banco,
                cambio
        );

        Transacao salva = transacaoGateway.salvar(transacao);
        publicarTransacaoGateway.publicarTransacao(salva);

        return salva;
    }
}
