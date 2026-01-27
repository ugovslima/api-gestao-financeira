package com.api_gestao_financeira.transaction_api.infra.controller;

import com.api.gestaofinanceira.common.enums.FormaPagamento;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final CriarTransacaoPendenteUseCase criarTransacaoPendenteUseCase;
    private final BuscarTransacaoPorIdUseCase buscarTransacaoPorIdUseCase;
    private final CriarRegistroUseCase criarRegistroUseCase;
    private final GerarRelatorioDespesasUseCase gerarRelatorioDespesasUseCase;

    public TransacaoController(CriarTransacaoPendenteUseCase criarTransacaoPendenteUseCase,
                               BuscarTransacaoPorIdUseCase buscarTransacaoPorIdUseCase, CriarRegistroUseCase criarRegistroUseCase, GerarRelatorioDespesasUseCase gerarRelatorioDespesasUseCase) {
        this.criarTransacaoPendenteUseCase = criarTransacaoPendenteUseCase;
        this.buscarTransacaoPorIdUseCase = buscarTransacaoPorIdUseCase;
        this.criarRegistroUseCase = criarRegistroUseCase;
        this.gerarRelatorioDespesasUseCase = gerarRelatorioDespesasUseCase;
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

    @GetMapping("/resumo")
    public ResponseEntity<RelatorioDespesas> gerarResumo(
            @RequestParam Long usuarioId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataReferencia,
            @RequestParam TipoPeriodo tipoPeriodo,
            @RequestParam(required = false) FormaPagamento formaPagamento
    ) {

        RelatorioDespesas resumo =
                gerarRelatorioDespesasUseCase.executar(
                        usuarioId,
                        dataReferencia,
                        tipoPeriodo,
                        formaPagamento
                );

        return ResponseEntity.ok(resumo);
    }
}
