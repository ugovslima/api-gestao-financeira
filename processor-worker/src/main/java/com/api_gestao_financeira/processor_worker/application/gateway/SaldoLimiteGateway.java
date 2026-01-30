package com.api_gestao_financeira.processor_worker.application.gateway;

import com.api_gestao_financeira.processor_worker.application.dto.SaldoLimite;

public interface SaldoLimiteGateway {
    SaldoLimite consultarPorBanco(String banco, Long usuarioId);
}

