package com.api_gestao_financeira.processor_worker.core.domain;

import java.math.BigDecimal;

public class Transacao {

    private Long id;
    private String banco;
    private String formaPagamento;
    private BigDecimal valor;
    private String status;
    private String motivo;

    public Long getId() {
        return id;
    }

    public String getBanco() {
        return banco;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getStatus() {
        return status;
    }

    public String getMotivo() {
        return motivo;
    }

    public void aprovarStatus() {
        this.status = "APROVADA";
    }

    public void recusarStatus() {
        this.status = "RECUSADA";
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
