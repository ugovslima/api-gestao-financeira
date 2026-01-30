package com.api_gestao_financeira.transaction_api.usecaseTestes;

import com.api_gestao_financeira.transaction_api.application.gateway.CambioGateway;
import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.application.usecase.CriarRegistroUseCase;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api_gestao_financeira.transaction_api.core.enums.Banco;
import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.core.enums.Moeda;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CriarRegistroTeste {

    @Mock
    private TransacaoGateway transacaoGateway;

    @Mock
    private CambioGateway cambioGateway;

    @InjectMocks
    private CriarRegistroUseCase criarRegistroUseCase;

    @Test
    void deveRegistrarESalvar() {
        Long usuarioId = 1L;
        FormaPagamento formaPagamento = FormaPagamento.DINHEIRO;
        BigDecimal valorOriginal = new BigDecimal("200.00");
        LocalDate data = LocalDate.of(2026, 1, 29); // quinta-feira, fim de semana não interfere
        String descricao = "Registro teste";
        Integer parcelas = 1;
        Banco banco = Banco.ITAU;
        Moeda moeda = Moeda.USD;

        when(cambioGateway.buscarTaxa(eq(moeda), any(LocalDate.class))).thenReturn(new BigDecimal("5.0"));
        when(transacaoGateway.salvar(any(Transacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transacao resultado = criarRegistroUseCase.executar(
                usuarioId,
                formaPagamento,
                valorOriginal,
                data,
                descricao,
                parcelas,
                banco,
                moeda
        );

        assertNotNull(resultado);
        assertEquals(usuarioId, resultado.getUsuarioId());
        assertEquals(formaPagamento, resultado.getFormaPagamento());
        assertEquals(data, resultado.getData());
        assertEquals(descricao, resultado.getDescricao());

        BigDecimal valorEsperado = valorOriginal.multiply(new BigDecimal("5.0"));
        assertEquals(0, valorEsperado.compareTo(resultado.getValor()));

        verify(cambioGateway, times(1)).buscarTaxa(eq(moeda), any(LocalDate.class));
        verify(transacaoGateway, times(1)).salvar(any(Transacao.class));
    }
}