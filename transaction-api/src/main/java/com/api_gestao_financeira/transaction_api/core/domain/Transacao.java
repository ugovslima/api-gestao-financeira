package com.api_gestao_financeira.transaction_api.core.domain;

import com.api_gestao_financeira.transaction_api.core.enums.Banco;
import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.enums.StatusTransacao;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Cambio;
import com.api_gestao_financeira.transaction_api.core.valueObjects.Parcelas;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transacao {

    private Long id;
    private Long usuarioId;
    private FormaPagamento formaPagamento;
    private BigDecimal valor;
    private LocalDate data;
    private String descricao;
    private Parcelas parcelas;
    private Banco banco;
    private StatusTransacao status;
    private Cambio cambio;

    private String motivo;

    public Transacao(
            Long usuarioId,
            FormaPagamento formaPagamento,
            BigDecimal valor,
            LocalDate data,
            String descricao,
            Parcelas parcelas,
            Banco banco,
            StatusTransacao status,
            Cambio cambio
    ) {
        this.usuarioId = usuarioId;
        this.formaPagamento = formaPagamento;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.parcelas = parcelas;
        this.banco = banco;
        this.status = status;
        this.cambio = cambio;
    }

    public static Transacao criarPendente(
            Long usuarioId,
            FormaPagamento formaPagamento,
            BigDecimal valor,
            LocalDate data,
            String descricao,
            Parcelas parcelas,
            Banco banco,
            Cambio cambio
    ) {
        return new Transacao(
                usuarioId,
                formaPagamento,
                valor,
                data,
                descricao,
                parcelas,
                banco,
                StatusTransacao.PENDENTE,
                cambio
        );
    }

    public static Transacao registrar(
            Long usuarioId,
            FormaPagamento formaPagamento,
            BigDecimal valor,
            LocalDate data,
            String descricao,
            Parcelas parcelas,
            Banco banco,
            Cambio cambio
    ) {
        return new Transacao(
                usuarioId,
                formaPagamento,
                valor,
                data,
                descricao,
                parcelas,
                banco,
                StatusTransacao.REGISTRADA,
                cambio
        );
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public Parcelas getParcelas() {
        return parcelas;
    }

    public Banco getBanco() {
        return banco;
    }

    public StatusTransacao getStatus() {
        return status;
    }

    public Cambio getCambio() {
        return cambio;
    }

    public void atribuirId(Long id) {
        this.id = id;
    }

    public void atribuirStatus(StatusTransacao status){
        this.status = status;
    }

    public String getMotivo() {
        return motivo;
    }

    public void atribuirMotivo(String motivo) {
        this.motivo = motivo;
    }

    public boolean validaParaRelatorio() {
        return status == StatusTransacao.APROVADA
                || status == StatusTransacao.REGISTRADA;
    }
}
