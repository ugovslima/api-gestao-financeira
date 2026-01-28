package com.api_gestao_financeira.transaction_api.infra.controller;

import com.api.gestaofinanceira.common.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.application.auth.UsuarioAutenticado;
import com.api_gestao_financeira.transaction_api.application.dto.RelatorioDespesas;
import com.api_gestao_financeira.transaction_api.application.dto.TipoPeriodo;
import com.api_gestao_financeira.transaction_api.application.usecase.BuscarTransacaoPorIdUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.CriarRegistroUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.CriarTransacaoPendenteUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.GerarRelatorioDespesasUseCase;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api_gestao_financeira.transaction_api.infra.dto.CriarRegistroRequest;
import com.api_gestao_financeira.transaction_api.infra.dto.CriarTransacaoRequest;
import com.api_gestao_financeira.transaction_api.infra.dto.TransacaoProcessadaResponse;
import com.api_gestao_financeira.transaction_api.infra.dto.TransacaoResponse;
import com.api_gestao_financeira.transaction_api.infra.persistence.mapper.TransacaoMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final CriarTransacaoPendenteUseCase criarTransacaoPendenteUseCase;
    private final BuscarTransacaoPorIdUseCase buscarTransacaoPorIdUseCase;
    private final CriarRegistroUseCase criarRegistroUseCase;

    public TransacaoController(CriarTransacaoPendenteUseCase criarTransacaoPendenteUseCase,
                               BuscarTransacaoPorIdUseCase buscarTransacaoPorIdUseCase, CriarRegistroUseCase criarRegistroUseCase, GerarRelatorioDespesasUseCase gerarRelatorioDespesasUseCase) {
        this.criarTransacaoPendenteUseCase = criarTransacaoPendenteUseCase;
        this.buscarTransacaoPorIdUseCase = buscarTransacaoPorIdUseCase;
        this.criarRegistroUseCase = criarRegistroUseCase;
    }

    @PostMapping()
    public ResponseEntity<TransacaoResponse> criar(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @Valid @RequestBody CriarTransacaoRequest request) {

        Long usuarioId = usuarioAutenticado.getId();

        var transacao = criarTransacaoPendenteUseCase.executar(
                usuarioId,
                request.formaPagamento(),
                request.valor(),
                null,
                request.descricao(),
                request.parcelas(),
                request.banco(),
                request.moeda()
        );

        return ResponseEntity.status(202).body(TransacaoMapper.toResponse(transacao));
    }

    @PostMapping("/registrar")
    public ResponseEntity<TransacaoResponse> registrar(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @Valid @RequestBody CriarRegistroRequest request) {

        Long usuarioId = usuarioAutenticado.getId();
        var transacao = criarRegistroUseCase.executar(
                usuarioId,
                request.formaPagamento(),
                request.valor(),
                request.data(),
                request.descricao(),
                request.parcelas(),
                request.banco(),
                request.moeda()
        );
        return ResponseEntity.status(201).body(TransacaoMapper.toResponse(transacao));
    }

    @GetMapping("/{transacaoId}")
    public ResponseEntity<TransacaoProcessadaResponse> buscar(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @PathVariable Long transacaoId) {

        Long usuarioId = usuarioAutenticado.getId();
        Transacao transacao = buscarTransacaoPorIdUseCase.executar(transacaoId);

        if (!transacao.getUsuarioId().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(TransacaoMapper.toProcessadaResponse(transacao));
    }

}
