package com.api_gestao_financeira.transaction_api.core.valueObjects;

import com.api_gestao_financeira.transaction_api.core.enums.Moeda;

import java.math.BigDecimal;

public class Cambio {

    private final Moeda moeda;
    private final BigDecimal taxa;

    private Cambio(Moeda moeda, BigDecimal taxa) {
        this.moeda = moeda;
        this.taxa = taxa;
    }

    public static Cambio criar(Moeda moeda, BigDecimal taxa) {
        if (moeda == null || moeda == Moeda.BRL) {
            return new Cambio(Moeda.BRL, BigDecimal.ONE);
        }

        if (taxa == null || taxa.signum() <= 0) {
            throw new IllegalArgumentException("Taxa de câmbio inválida para moeda " + moeda);
        }

        return new Cambio(moeda, taxa);
    }

    public Moeda getMoeda() {
        return moeda;
    }

    public BigDecimal getTaxa() {
        return taxa;
    }
}