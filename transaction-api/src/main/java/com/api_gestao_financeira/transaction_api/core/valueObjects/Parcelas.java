package com.api_gestao_financeira.transaction_api.core.valueObjects;

public class Parcelas {

    private final int quantidade;

    private Parcelas(int quantidade) {
        this.quantidade = quantidade;
    }

    public static Parcelas criar(FormaPagamento formaPagamento, Integer parcelas) {

        if (formaPagamento != FormaPagamento.CREDITO) {
            return new Parcelas(1);
        }

        if (parcelas == null || parcelas < 1) {
            return new Parcelas(1);
        }

        return new Parcelas(parcelas);
    }

    public int getQuantidade() {
        return quantidade;
    }
}