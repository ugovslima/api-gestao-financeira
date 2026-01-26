package com.api_gestao_financeira.transaction_api.application.gateway;

import com.api_gestao_financeira.transaction_api.core.enums.Moeda;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CambioGateway {
    BigDecimal buscarTaxa(Moeda moeda, LocalDate data);
}
