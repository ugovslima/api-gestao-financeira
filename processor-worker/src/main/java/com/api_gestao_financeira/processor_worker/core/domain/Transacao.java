package com.api_gestao_financeira.processor_worker.core.domain;

import com.api.gestaofinanceira.common.enums.Banco;
import com.api.gestaofinanceira.common.enums.FormaPagamento;
import com.api.gestaofinanceira.common.enums.StatusTransacao;

import java.math.BigDecimal;

public class Transacao {

    private Long id;
    private Long usuarioId;
    private FormaPagamento formaPagamento;
    private BigDecimal valor;
    private StatusTransacao status;
    private Banco banco;
    private String motivo;

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
