package com.sicredi.service;

import com.sicredi.dto.Conta;
import com.sicredi.enums.Colunas;
import com.sicredi.enums.Status;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static javax.swing.text.TabStop.ALIGN_CENTER;
import static org.apache.poi.hssf.record.ExtendedFormatRecord.VERTICAL_CENTER;
import static org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND;

public class ContaService {

    public static void enviarContasReceita() {
        List<Conta> contaList = carregarArquivo();

        contaList.forEach(conta -> {
            try {
                conta.setAtualizado(ReceitaService.atualizarConta(conta.getAgencia(), conta.getConta(), conta.getSaldo(), conta.getStatus().name()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        atualizarRetorno(contaList);
    }

    private static void atualizarRetorno(List<Conta> contaList) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("RetornoReceita");

        sheet.setDefaultColumnWidth(15);
        sheet.setDefaultRowHeight((short) 400);

        int rownum = 0;
        int cellnum = 0;
        Cell cell;
        Row row;

        HSSFDataFormat numberFormat = workbook.createDataFormat();

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.forInt(ALIGN_CENTER));
        headerStyle.setVerticalAlignment(VerticalAlignment.forInt(VERTICAL_CENTER));

        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setAlignment(HorizontalAlignment.forInt(ALIGN_CENTER));
        textStyle.setVerticalAlignment(VerticalAlignment.forInt(VERTICAL_CENTER));

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(numberFormat.getFormat("#,## 0.00"));
        numberStyle.setVerticalAlignment(VerticalAlignment.forInt(VERTICAL_CENTER));

        row = sheet.createRow(rownum++);
        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("agencia");

        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("conta");

        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("saldo");

        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("status");

        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("retorno");

        // Adicionando os dados dos produtos na planilha
        for (Conta conta : contaList) {
            row = sheet.createRow(rownum++);
            cellnum = 0;

            cell = row.createCell(cellnum++);
            cell.setCellStyle(textStyle);
            cell.setCellValue(conta.getConta());

            cell = row.createCell(cellnum++);
            cell.setCellStyle(textStyle);
            cell.setCellValue(conta.getConta());

            cell = row.createCell(cellnum++);
            cell.setCellStyle(numberStyle);
            cell.setCellValue(conta.getSaldo());

            cell = row.createCell(cellnum++);
            cell.setCellStyle(textStyle);
            cell.setCellValue(conta.getStatus().name());

            cell = row.createCell(cellnum++);
            cell.setCellStyle(textStyle);
            cell.setCellValue(conta.isAtualizado() ? "Atualizado" : "Desatualizado");

        }

        try {

            //Escrevendo o arquivo em disco
            FileOutputStream out = new FileOutputStream(new File("C:\\Users\\PC-000\\Downloads\\RetornoReceita.xls"));
            workbook.write(out);
            out.close();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Conta> carregarArquivo() {

        List<Conta> listaContas = new ArrayList<>();

        try {
            FileInputStream arquivo = new FileInputStream("C:\\Users\\PC-000\\Downloads\\sicredi.xls");

            HSSFWorkbook workbook = new HSSFWorkbook(arquivo);
            HSSFSheet sheetAlunos = workbook.getSheetAt(0);

            for (Row row : sheetAlunos) {
                Iterator<Cell> cellIterator = row.cellIterator();

                Conta conta = new Conta();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if (!isValue(cell)) continue;

                    switch (cell.getColumnIndex()) {
                        case 0:
                            conta.setAgencia(getAgencia(cell));
                            break;
                        case 1:
                            conta.setConta(getConta(cell));
                            break;
                        case 2:
                            conta.setSaldo(cell.getNumericCellValue());
                            break;
                        case 3:
                            conta.setStatus(Status.valueOf(cell.getStringCellValue()));
                            break;
                    }

                    if (conta.getStatus() != null) {
                        listaContas.add(conta);
                    }
                }
            }
            arquivo.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Arquivo Excel não encontrado!");
        }
        return listaContas;
    }

    private static String getConta(Cell cell) {
        String conta = cell.getStringCellValue();
        conta = conta.replaceAll("[-]", "");

        StringBuilder sb = getStringBuilder(conta);
        return sb.length() < 6 ? acrescentaZero(6, sb.toString()) : sb.toString();
    }

    private static String getAgencia(Cell cell) {
        String agencia = String.valueOf(cell.getNumericCellValue());

        StringBuilder sb = getStringBuilder(agencia);
        return sb.length() < 4 ? acrescentaZero(4, sb.toString()) : sb.toString();
    }

    private static StringBuilder getStringBuilder(String agencia) {
        StringBuilder sb = new StringBuilder(agencia);
        return sb.delete(sb.length() - 2, sb.length());
    }

    private static String acrescentaZero(int qtdDigitos, String num) {
        StringBuilder numBuilder = new StringBuilder(num);

        for (int i = 0; i < qtdDigitos; i++) {
            if (numBuilder.length() < qtdDigitos) {
                numBuilder.insert(0, "0");
            }
        }
        return numBuilder.toString();
    }

    private static boolean isValue(Cell cell) {

        Colunas coluna = getColuna(cell);

        if (!cell.getCellType().name().equals("STRING") || coluna == null) return true;

        return !Arrays.asList(Colunas.values()).contains(coluna);
    }

    private static Colunas getColuna(Cell cell) {
        try {
            return Colunas.valueOf(cell.getStringCellValue());
        } catch (Exception e) {
            return null;
        }
    }
}
