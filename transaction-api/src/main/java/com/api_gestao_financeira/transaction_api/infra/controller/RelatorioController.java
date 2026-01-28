package com.api_gestao_financeira.transaction_api.infra.controller;

import com.api.gestaofinanceira.common.enums.FormaPagamento;
import com.api_gestao_financeira.transaction_api.application.auth.UsuarioAutenticado;
import com.api_gestao_financeira.transaction_api.application.dto.RelatorioDespesas;
import com.api_gestao_financeira.transaction_api.application.dto.RelatorioDespesasPlanilha;
import com.api_gestao_financeira.transaction_api.application.dto.TipoPeriodo;
import com.api_gestao_financeira.transaction_api.application.usecase.GerarRelatorioDespesasPlanilhaUseCase;
import com.api_gestao_financeira.transaction_api.application.usecase.GerarRelatorioDespesasUseCase;
import com.api_gestao_financeira.transaction_api.infra.gateway.RelatorioDespesasPlanilhaExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final GerarRelatorioDespesasUseCase gerarRelatorioDespesasUseCase;
    private final GerarRelatorioDespesasPlanilhaUseCase gerarRelatorioDespesasPlanilhaUseCase;

    public RelatorioController(GerarRelatorioDespesasUseCase gerarRelatorioDespesasUseCase, GerarRelatorioDespesasPlanilhaUseCase gerarRelatorioDespesasPlanilhaUseCase) {
        this.gerarRelatorioDespesasUseCase = gerarRelatorioDespesasUseCase;
        this.gerarRelatorioDespesasPlanilhaUseCase = gerarRelatorioDespesasPlanilhaUseCase;
    }

    @GetMapping("/resumo")
    public ResponseEntity<RelatorioDespesas> gerarResumo(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataReferencia,
            @RequestParam TipoPeriodo tipoPeriodo,
            @RequestParam(required = false) FormaPagamento formaPagamento
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

    @GetMapping("/excel")
    public void gerarExcel(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataReferencia,
            HttpServletResponse response
    ) throws Exception {
        RelatorioDespesasPlanilha relatorio = gerarRelatorioDespesasPlanilhaUseCase.executar(usuarioAutenticado.getId(), dataReferencia);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        YearMonth ym = YearMonth.from(dataReferencia);
        response.setHeader("Content-Disposition", "attachment; filename=relatorio_despesas"+ym+".xlsx");

        RelatorioDespesasPlanilhaExporter exporter = new RelatorioDespesasPlanilhaExporter();
        exporter.exportar(relatorio, response.getOutputStream());
    }
}
