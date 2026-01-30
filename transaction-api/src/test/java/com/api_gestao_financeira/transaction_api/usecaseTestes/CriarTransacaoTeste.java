package com.api_gestao_financeira.transaction_api.usecaseTestes;

import com.api_gestao_financeira.transaction_api.application.gateway.CambioGateway;
import com.api_gestao_financeira.transaction_api.application.gateway.PublicarTransacaoGateway;
import com.api_gestao_financeira.transaction_api.application.gateway.TransacaoGateway;
import com.api_gestao_financeira.transaction_api.application.usecase.CriarTransacaoUseCase;
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

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarTransacaoUseCaseTest {

    @Mock
    private TransacaoGateway transacaoGateway;

    @Mock
    private PublicarTransacaoGateway publicarTransacaoGateway;

    @Mock
    private CambioGateway cambioGateway;

    @InjectMocks
    private CriarTransacaoUseCase criarTransacaoUseCase;

    @Test
    void deveCriarTransacaoEPublicar() {
        Long usuarioId = 1L;
        FormaPagamento formaPagamento = FormaPagamento.CREDITO;
        BigDecimal valor = new BigDecimal("100.00");
        LocalDate data = LocalDate.now();
        String descricao = "Compra teste";
        Integer parcelas = 1;
        Banco banco = Banco.ITAU;
        Moeda moeda = Moeda.EUR;

        BigDecimal taxaCambio = BigDecimal.ONE;
        when(cambioGateway.buscarTaxa(any(Moeda.class), any(LocalDate.class))).thenReturn(BigDecimal.ONE);
        when(transacaoGateway.salvar(any(Transacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transacao resultado = criarTransacaoUseCase.executar(
                usuarioId,
                formaPagamento,
                valor,
                data,
                descricao,
                parcelas,
                banco,
                moeda
        );

        assertNotNull(resultado, "Transação deve ser retornada");
        assertEquals(usuarioId, resultado.getUsuarioId());
        assertEquals(formaPagamento, resultado.getFormaPagamento());
        assertEquals(valor.multiply(taxaCambio), resultado.getValor());

        verify(transacaoGateway, times(1)).salvar(any(Transacao.class));

        ArgumentCaptor<Transacao> captor = ArgumentCaptor.forClass(Transacao.class);
        verify(publicarTransacaoGateway, times(1)).publicarTransacao(captor.capture());

        Transacao publicada = captor.getValue();
        assertEquals(resultado, publicada);
    }
}
