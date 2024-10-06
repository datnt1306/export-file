package com.example.demo.service;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;


@Service
public class ExcelService {

    @Value("${template.exel}")
    private String template;

    private Map<String, Object> getDataFromDatabase() {
        Map<String, Object> data = new HashMap<>();
        data.put("Tên đơn vị", "Phòng Kế toán");
        data.put("ngày tạo", "01/01/2024");
        data.put("Tháng", "Tháng 10");
        data.put("Mã rủi ro", "RR123");
        data.put("Tên rủi ro", "Rủi ro tài chính");
        data.put("CSH RR", "Nguyễn Văn A");
        data.put("ĐVCTXLRR", "Phòng Tài chính");
        return data;
    }

    @SneakyThrows
    public ByteArrayOutputStream generateExel() {
        Map<String, Object> data =  this.getDataFromDatabase();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        FileInputStream fileInputStream = new FileInputStream(template);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING) {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.contains("<") && cellValue.contains(">")) {
                        String key = cellValue.substring(cellValue.indexOf("<") + 1, cellValue.indexOf(">"));
                        Object value = data.get(key);
                        if (value != null) {
                            cell.setCellValue(cellValue.replace("<" + key + ">", value.toString()));
                        }
                    }
                }
            }
        }
        workbook.write(outputStream);
        return outputStream;
    }
}
