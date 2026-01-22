package com.api_gestao_financeira.transaction_api.infra.controller;

import com.api_gestao_financeira.transaction_api.application.usecase.BuscarTransacaoPorIdUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.CriarTransacaoUseCase;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api_gestao_financeira.transaction_api.infra.dto.CriarTransacaoRequest;
import com.api_gestao_financeira.transaction_api.infra.dto.TransacaoResponse;
import com.api_gestao_financeira.transaction_api.infra.persistence.mapper.TransacaoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final CriarTransacaoUseCase criarTransacaoUseCase;
    private final BuscarTransacaoPorIdUseCase buscarTransacaoPorIdUseCase;

    public TransacaoController(CriarTransacaoUseCase criarTransacaoUseCase,
                               BuscarTransacaoPorIdUseCase buscarTransacaoPorIdUseCase) {
        this.criarTransacaoUseCase = criarTransacaoUseCase;
        this.buscarTransacaoPorIdUseCase = buscarTransacaoPorIdUseCase;
    }

    @PostMapping
    public ResponseEntity<TransacaoResponse> criar(@RequestBody CriarTransacaoRequest request) {
        var transacao = criarTransacaoUseCase.executar(
                request.usuarioId(),
                request.formaPagamento(),
                request.valor(),
                request.data(),
                request.descricao(),
                request.parcelas(),
                request.banco()
        );

        return ResponseEntity.ok(TransacaoMapper.toResponse(transacao));
    }

    @GetMapping("/{transacaoId}")
    public ResponseEntity<TransacaoResponse> buscar(@PathVariable Long transacaoId){
        Transacao transacao = buscarTransacaoPorIdUseCase.executar(transacaoId);
        return ResponseEntity.ok(TransacaoMapper.toResponse(transacao));
    }
}
