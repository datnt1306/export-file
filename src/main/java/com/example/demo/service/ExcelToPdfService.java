package com.example.demo.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Paragraph;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ExcelToPdfService {

    public String convertExcelToPdf(InputStream excelFileInputStream) throws Exception {
        // Tạo file PDF tạm thời
        String pdfFilePath = "temporary_output.pdf";
        Document pdfDocument = new Document();

        try {
            PdfWriter.getInstance(pdfDocument, new FileOutputStream(pdfFilePath));
            pdfDocument.open();

            Workbook workbook = new XSSFWorkbook(excelFileInputStream);
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                pdfDocument.add(new Paragraph("Sheet: " + sheet.getSheetName()));

                for (Row row : sheet) {
                    StringBuilder rowData = new StringBuilder();
                    for (Cell cell : row) {
                        rowData.append(getCellValue(cell)).append("\t");
                    }
                    pdfDocument.add(new Paragraph(rowData.toString()));
                }
            }
        } finally {
            pdfDocument.close();
        }

        return pdfFilePath; // Trả về đường dẫn file PDF đã tạo
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
