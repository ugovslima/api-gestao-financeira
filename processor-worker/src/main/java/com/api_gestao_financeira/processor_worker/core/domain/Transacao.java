package com.api_gestao_financeira.processor_worker.core.domain;

import com.api_gestao_financeira.processor_worker.core.enums.Banco;
import com.api_gestao_financeira.processor_worker.core.enums.FormaPagamento;
import com.api_gestao_financeira.processor_worker.core.enums.StatusTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transacao {

    private Long id;
    private Long usuarioId;
    private FormaPagamento formaPagamento;
    private BigDecimal valor;
    private StatusTransacao status;
    private Banco banco;
    private String motivo;

    public Transacao(Long id, Long usuarioId, FormaPagamento formaPagamento, BigDecimal valor, StatusTransacao status, Banco banco) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.formaPagamento = formaPagamento;
        this.valor = valor;
        this.status = status;
        this.banco = banco;
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

    public StatusTransacao getStatus() {
        return status;
    }

    public Banco getBanco() {
        return banco;
    }

    public String getMotivo() {
        return motivo;
    }

    public void aprovarStatus() {
        this.status = StatusTransacao.APROVADA;
    }

    public void recusarStatus() {
        this.status = StatusTransacao.RECUSADA;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }




}
