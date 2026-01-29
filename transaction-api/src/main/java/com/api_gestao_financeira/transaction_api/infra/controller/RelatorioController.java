package com.api_gestao_financeira.transaction_api.infra.controller;

import com.api_gestao_financeira.transaction_api.application.auth.UsuarioAutenticado;
import com.api_gestao_financeira.transaction_api.application.dto.RelatorioDespesas;
import com.api_gestao_financeira.transaction_api.application.dto.RelatorioDespesasPlanilha;
import com.api_gestao_financeira.transaction_api.application.dto.TipoPeriodo;
import com.api_gestao_financeira.transaction_api.application.usecase.GerarRelatorioDespesasPlanilhaUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.GerarRelatorioDespesasUseCase;
import com.api_gestao_financeira.transaction_api.core.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.infra.gateway.RelatorioDespesasPlanilhaExporter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.YearMonth;

@Tag(name = "Relátorios", description = "Endpoints de relátorios financeiros")
@SecurityRequirement(name = "bearer-key")
@RestController
@Validated
@RequestMapping("/relatorios")
public class RelatorioController {

    private final GerarRelatorioDespesasUseCase gerarRelatorioDespesasUseCase;
    private final GerarRelatorioDespesasPlanilhaUseCase gerarRelatorioDespesasPlanilhaUseCase;

    public RelatorioController(GerarRelatorioDespesasUseCase gerarRelatorioDespesasUseCase, GerarRelatorioDespesasPlanilhaUseCase gerarRelatorioDespesasPlanilhaUseCase) {
        this.gerarRelatorioDespesasUseCase = gerarRelatorioDespesasUseCase;
        this.gerarRelatorioDespesasPlanilhaUseCase = gerarRelatorioDespesasPlanilhaUseCase;
    }

    @Operation(
            summary = "Gerar resumo de despesas",
            description = "Retorna um resumo das despesas do usuário com base na data de referência, tipo de período e forma de pagamento opcional. O usuário é definido pelo token JWT"
    )
    @ApiResponse(responseCode = "200", description = "Resumo gerado com sucesso")
    @Parameters({
            @Parameter(
                    name = "dataReferencia",
                    description = "Data de referência no formato yyyy-MM-dd",
                    example = "2026-01-01",
                    required = true
            ),
            @Parameter(
                    name = "tipoPeriodo",
                    description = "Tipo do período do relatório",
                    example = "MENSAL",
                    required = true
            ),
            @Parameter(
                    name = "formaPagamento",
                    description = "Forma de pagamento para filtro (opcional)",
                    example = "CREDITO",
                    required = false
            )
    })
    @GetMapping("/resumo")
    public ResponseEntity<RelatorioDespesas> gerarResumo(

            @Parameter(hidden = true)
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,

            @NotNull
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataReferencia,

            @NotNull
            @RequestParam
            TipoPeriodo tipoPeriodo,

            @RequestParam(required = false)
            FormaPagamento formaPagamento
    ) {

        RelatorioDespesas resumo =
                gerarRelatorioDespesasUseCase.executar(
                        usuarioAutenticado.getId(),
                        dataReferencia,
                        tipoPeriodo,
                        formaPagamento
                );

        return ResponseEntity.ok(resumo);
    }

    @Operation(
            summary = "Gerar relatório de despesas em Excel",
            description = "Gera e retorna um arquivo Excel contendo o relatório de despesas do usuário para o mês/ano informado. O usuário é definido pelo token JWT"
    )
    @ApiResponse(responseCode = "200", description = "Arquivo gerado com sucesso")
    @GetMapping("/excel")
    public ResponseEntity<byte[]> gerarExcel(
            @Parameter(
                    description = "Data de referência no formato yyyy-MM-dd",
                    example = "2026-01-01",
                    required = true)
            @NotNull
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @PastOrPresent
            @Valid
            LocalDate dataReferencia,

            @Parameter(hidden = true)
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado
    ) throws Exception {

        RelatorioDespesasPlanilha relatorio =
                gerarRelatorioDespesasPlanilhaUseCase.executar(usuarioAutenticado.getId(), dataReferencia);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RelatorioDespesasPlanilhaExporter exporter = new RelatorioDespesasPlanilhaExporter();
        exporter.exportar(relatorio, baos);

        byte[] arquivo = baos.toByteArray();

        YearMonth ym = YearMonth.from(dataReferencia);
        String nomeArquivo = "relatorio_despesas_" + ym + ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(ContentDisposition.attachment().filename(nomeArquivo).build());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(arquivo);
    }
}
