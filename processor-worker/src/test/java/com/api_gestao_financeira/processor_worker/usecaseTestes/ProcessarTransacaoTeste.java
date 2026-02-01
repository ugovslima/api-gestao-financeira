package com.api_gestao_financeira.processor_worker.usecaseTestes;

import com.api_gestao_financeira.processor_worker.application.dto.SaldoLimite;
import com.api_gestao_financeira.processor_worker.application.gateway.SaldoLimiteGateway;
import com.api_gestao_financeira.processor_worker.application.gateway.TransacaoRepository;
import com.api_gestao_financeira.processor_worker.application.usecase.ProcessarTransacaoUseCase;
import com.api_gestao_financeira.processor_worker.core.domain.Transacao;
import com.api_gestao_financeira.processor_worker.core.enums.Banco;
import com.api_gestao_financeira.processor_worker.core.enums.FormaPagamento;
import com.api_gestao_financeira.processor_worker.core.enums.StatusTransacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessarTransacaoTeste {

    @Mock
    private SaldoLimiteGateway saldoLimiteGateway;

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private ProcessarTransacaoUseCase useCase;

    private Transacao transacao;

    @BeforeEach
    void setup() {
        transacao = new Transacao(
                1L,
                10L,
                FormaPagamento.CREDITO,
                new BigDecimal("100.00"),
                StatusTransacao.PENDENTE,
                Banco.ITAU
        );
    }

    @Test
    void deveDeveAprovarLimiteDisponivel() {
        SaldoLimite saldoLimite = new SaldoLimite(
                new BigDecimal("0.00"),
                new BigDecimal("500.00")
        );

        when(saldoLimiteGateway.consultarPorBanco(
                Banco.ITAU.name(), 10L
        )).thenReturn(saldoLimite);

        useCase.executar(transacao);

        assertEquals(StatusTransacao.APROVADA, transacao.getStatus());
        assertNotNull(transacao.getMotivo());
        assertTrue(transacao.getMotivo().contains("Limite disponível"));

        verify(transacaoRepository).atualizar(transacao);
        verifyNoMoreInteractions(transacaoRepository);
    }

    @Test
    void deveRecusarSaldoInisuficiente() {
        transacao = new Transacao(
                2L,
                10L,
                FormaPagamento.DEBITO,
                new BigDecimal("100.00"),
                StatusTransacao.PENDENTE,
                Banco.ITAU
        );

        SaldoLimite saldoLimite = new SaldoLimite(
                new BigDecimal("50.00"),
                new BigDecimal("0.00")
        );

        when(saldoLimiteGateway.consultarPorBanco(
                Banco.ITAU.name(), 10L
        )).thenReturn(saldoLimite);

        useCase.executar(transacao);

        assertEquals(StatusTransacao.RECUSADA, transacao.getStatus());
        assertNotNull(transacao.getMotivo());
        assertTrue(transacao.getMotivo().contains("Saldo insuficiente"));

        verify(transacaoRepository).atualizar(transacao);
        verifyNoMoreInteractions(transacaoRepository);
    }
}
