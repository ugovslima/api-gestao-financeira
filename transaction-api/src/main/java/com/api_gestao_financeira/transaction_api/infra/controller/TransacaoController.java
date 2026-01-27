package com.api_gestao_financeira.transaction_api.infra.controller;

import com.api_gestao_financeira.transaction_api.application.usecase.BuscarTransacaoPorIdUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.CriarRegistroUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.CriarTransacaoPendenteUseCase;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api_gestao_financeira.transaction_api.infra.dto.CriarRegistroRequest;
import com.api_gestao_financeira.transaction_api.infra.dto.CriarTransacaoRequest;
import com.api_gestao_financeira.transaction_api.infra.dto.TransacaoProcessadaResponse;
import com.api_gestao_financeira.transaction_api.infra.dto.TransacaoResponse;
import com.api_gestao_financeira.transaction_api.infra.persistence.mapper.TransacaoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final CriarTransacaoPendenteUseCase criarTransacaoPendenteUseCase;
    private final BuscarTransacaoPorIdUseCase buscarTransacaoPorIdUseCase;
    private final CriarRegistroUseCase criarRegistroUseCase;

    public TransacaoController(CriarTransacaoPendenteUseCase criarTransacaoPendenteUseCase,
                               BuscarTransacaoPorIdUseCase buscarTransacaoPorIdUseCase, CriarRegistroUseCase criarRegistroUseCase) {
        this.criarTransacaoPendenteUseCase = criarTransacaoPendenteUseCase;
        this.buscarTransacaoPorIdUseCase = buscarTransacaoPorIdUseCase;
        this.criarRegistroUseCase = criarRegistroUseCase;
    }

    @PostMapping()
    public ResponseEntity<TransacaoResponse> criar(@RequestBody CriarTransacaoRequest request) {
        var transacao = criarTransacaoPendenteUseCase.executar(
                request.usuarioId(),
                request.formaPagamento(),
                request.valor(),
                null,
                request.descricao(),
                request.parcelas(),
                request.banco(),
                request.moeda()
        );

        return ResponseEntity.ok(TransacaoMapper.toResponse(transacao));
    }

    @PostMapping("/registrar")
    public ResponseEntity<TransacaoResponse> registrar(@RequestBody CriarRegistroRequest request) {
        var transacao = criarRegistroUseCase.executar(
                request.usuarioId(),
                request.formaPagamento(),
                request.valor(),
                request.data(),
                request.descricao(),
                request.parcelas(),
                request.banco(),
                request.moeda()
        );

        return ResponseEntity.ok(TransacaoMapper.toResponse(transacao));
    }

    @GetMapping("/{transacaoId}")
    public ResponseEntity<TransacaoProcessadaResponse> buscar(@PathVariable Long transacaoId){
        Transacao transacao = buscarTransacaoPorIdUseCase.executar(transacaoId);
        return ResponseEntity.ok(TransacaoMapper.toProcessadaResponse(transacao));
    }
}
