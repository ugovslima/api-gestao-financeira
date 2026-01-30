package com.api_gestao_financeira.transaction_api.application.usecase;

import com.api_gestao_financeira.transaction_api.application.gateway.CambioGateway;
import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api_gestao_financeira.transaction_api.core.enums.Banco;
import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.enums.Moeda;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Cambio;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Parcelas;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CriarRegistroUseCase {

    private final TransacaoGateway transacaoGateway;
    private final CambioGateway cambioGateway;

    public CriarRegistroUseCase(TransacaoGateway transacaoGateway, CambioGateway cambioGateway) {
        this.transacaoGateway = transacaoGateway;
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

        LocalDate dataCambio = data.minusDays(1);

        while (dataCambio.getDayOfWeek().getValue() >= 6) {
            dataCambio = dataCambio.minusDays(1);
        }

        Moeda moedaFinal = moeda == null ? Moeda.BRL : moeda;

        BigDecimal taxa = cambioGateway.buscarTaxa(
                moedaFinal,
                dataCambio
        );

        Cambio cambio = Cambio.criar(moedaFinal, taxa);
        BigDecimal valorEmReais = valor.multiply(cambio.getTaxa());

        Transacao transacao = Transacao.registrar(
                usuarioId,
                formaPagamento,
                valorEmReais,
                data,
                descricao,
                Parcelas.criar(formaPagamento, parcelas),
                banco,
                cambio
        );

        Transacao salva = transacaoGateway.salvar(transacao);

        return salva;
    }
}
