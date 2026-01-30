package com.api_gestao_financeira.transaction_api.infra.controller;

import com.api_gestao_financeira.transaction_api.application.auth.UsuarioAutenticado;
import com.api_gestao_financeira.transaction_api.application.usecase.BuscarTransacaoPorIdUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.CriarRegistroUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.CriarTransacaoUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.GerarRelatorioDespesasUseCase;
import com.api_gestao_financeira.transaction_api.core.domain.Transacao;
import com.api_gestao_financeira.transaction_api.infra.dto.CriarRegistroRequest;
import com.api_gestao_financeira.transaction_api.infra.dto.CriarTransacaoRequest;
import com.api_gestao_financeira.transaction_api.infra.dto.TransacaoProcessadaResponse;
import com.api_gestao_financeira.transaction_api.infra.dto.TransacaoResponse;
import com.api_gestao_financeira.transaction_api.infra.persistence.mapper.TransacaoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Transações", description = "Operações de criação, registro e consulta de transações financeiras")
@SecurityRequirement(name = "bearer-key")
@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final CriarTransacaoUseCase criarTransacaoUseCase;
    private final BuscarTransacaoPorIdUseCase buscarTransacaoPorIdUseCase;
    private final CriarRegistroUseCase criarRegistroUseCase;

    public TransacaoController(CriarTransacaoUseCase criarTransacaoUseCase,
                               BuscarTransacaoPorIdUseCase buscarTransacaoPorIdUseCase, CriarRegistroUseCase criarRegistroUseCase, GerarRelatorioDespesasUseCase gerarRelatorioDespesasUseCase) {
        this.criarTransacaoUseCase = criarTransacaoUseCase;
        this.buscarTransacaoPorIdUseCase = buscarTransacaoPorIdUseCase;
        this.criarRegistroUseCase = criarRegistroUseCase;
    }

    @Operation(
            summary = "Criar transação",
            description = "Cria uma nova transação para analise no processor worker. O usuário é definido pelo token JWT"
    )
    @ApiResponse(responseCode = "202", description = "Transação criada e encaminhada para ser processada com sucesso")
    @PostMapping()
    public ResponseEntity<TransacaoResponse> criar(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @Valid @RequestBody CriarTransacaoRequest request) {

        Long usuarioId = usuarioAutenticado.getId();

        var transacao = criarTransacaoUseCase.executar(
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

    @Operation(
            summary = "Registrar transação manual",
            description = "Registra uma transação informando a data manualmente. O usuário é definido pelo token JWT" +
                    "Caso 'parcelas' seja null será considerado 1. " +
                    "Caso 'moeda' seja null será considerado BRL."
    )
    @ApiResponse(responseCode = "201", description = "Registro criado com sucesso")
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

    @Operation(
            summary = "Buscar transação por ID",
            description = "Retorna os detalhes de uma transação do usuário autenticado. " +
                    "Caso a transação não pertença ao usuário, será retornado 403."
    )
    @ApiResponse(responseCode = "202", description = "Registro criado com sucesso")
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
