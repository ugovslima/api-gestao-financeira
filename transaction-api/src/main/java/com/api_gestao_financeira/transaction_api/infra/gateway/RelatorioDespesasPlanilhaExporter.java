package com.api_gestao_financeira.transaction_api.infra.gateway;

import com.api_gestao_financeira.transaction_api.application.dto.LinhaRelatorio;
import com.api_gestao_financeira.transaction_api.application.dto.RelatorioDespesasPlanilha;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

public class RelatorioDespesasPlanilhaExporter {

    public void exportar(RelatorioDespesasPlanilha relatorio, OutputStream outputStream) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Despesas Mensais");

            int[][] tabelasColunas = {
                    {0, 3},
                    {5, 8},
                    {10, 13},
                    {15, 18}
            };

            String[] titulos = {"DEBITO", "CREDITO", "PIX", "DINHEIRO"};
            List<List<LinhaRelatorio>> dados = List.of(
                    relatorio.debito(),
                    relatorio.credito(),
                    relatorio.pix(),
                    relatorio.dinheiro()
            );
            List<BigDecimal> totais = List.of(
                    relatorio.totalDebito(),
                    relatorio.totalCredito(),
                    relatorio.totalPix(),
                    relatorio.totalDinheiro()
            );

            int linhaInicioTabela = 0;
            int maxLinhas = 0;

            for (int i = 0; i < 4; i++) {
                int colInicio = tabelasColunas[i][0];
                int colFim = tabelasColunas[i][1];

                criarCelula(sheet, linhaInicioTabela, colInicio, titulos[i], true);

                criarCelula(sheet, linhaInicioTabela + 1, colInicio, "Data", true);
                criarCelula(sheet, linhaInicioTabela + 1, colInicio + 1, "Banco", true);
                criarCelula(sheet, linhaInicioTabela + 1, colInicio + 2, "Descrição", true);
                criarCelula(sheet, linhaInicioTabela + 1, colInicio + 3, "Valor", true);

                List<LinhaRelatorio> linhas = dados.get(i);
                for (int j = 0; j < linhas.size(); j++) {
                    LinhaRelatorio linha = linhas.get(j);
                    int rowNum = linhaInicioTabela + 2 + j;
                    criarCelula(sheet, rowNum, colInicio, linha.data().toString(), false);
                    criarCelula(sheet, rowNum, colInicio + 1, linha.banco(), false);
                    criarCelula(sheet, rowNum, colInicio + 2, linha.descricao(), false);
                    criarCelula(sheet, rowNum, colInicio + 3, linha.valor().toString(), false);
                }

                int linhaTotal = linhaInicioTabela + 2 + linhas.size();
                criarCelula(sheet, linhaTotal, colInicio + 2, "TOTAL", true);
                criarCelula(sheet, linhaTotal, colInicio + 3, totais.get(i).toString(), true);

                if (linhas.size() > maxLinhas) maxLinhas = linhas.size();
            }

            int linhaTotalGeral = linhaInicioTabela + 3 + maxLinhas;
            criarCelula(sheet, linhaTotalGeral, 0, "TOTAL GERAL DO MÊS", true);
            criarCelula(sheet, linhaTotalGeral, 1, relatorio.totalGeral().toString(), true);

            for (int c = 0; c <= 18; c++) {
                sheet.autoSizeColumn(c);
            }

            workbook.write(outputStream);
        }
    }

    private void criarCelula(Sheet sheet, int linha, int coluna, String valor, boolean negrito) {
        Row row = sheet.getRow(linha);
        if (row == null) row = sheet.createRow(linha);
        Cell cell = row.createCell(coluna);
        cell.setCellValue(valor);

        if (negrito) {
            Workbook workbook = sheet.getWorkbook();
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }
    }
}

