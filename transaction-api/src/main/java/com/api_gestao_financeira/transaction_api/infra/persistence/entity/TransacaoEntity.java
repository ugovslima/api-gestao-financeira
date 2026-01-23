package com.api_gestao_financeira.transaction_api.infra.persistence.entity;

import com.api_gestao_financeira.transaction_api.core.enums.Banco;
import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.enums.StatusTransacao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transacoes")
public class TransacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    private BigDecimal valor;

    private LocalDate data;

    private String descricao;

    private Integer parcelas;

    @Enumerated(EnumType.STRING)
    private Banco banco;

    @Enumerated(EnumType.STRING)
    private StatusTransacao status;

    public TransacaoEntity() {
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

    public Integer getParcelas() {
        return parcelas;
    }

    public Banco getBanco() {
        return banco;
    }

    public StatusTransacao getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setParcelas(Integer parcelas) {
        this.parcelas = parcelas;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public void setStatus(StatusTransacao status) {
        this.status = status;
    }
}

