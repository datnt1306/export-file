package com.example.demo.controller;

import com.example.demo.service.ExcelToPdfService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/excel")
public class PdfController {

    private ExcelToPdfService excelToPdfService;

    @PostMapping("/convert")
    public ResponseEntity<FileSystemResource> convertExcelToPdf(@RequestParam("file") MultipartFile file) {
        try {
            // Chuyển đổi Excel sang PDF
            String pdfFilePath = excelToPdfService.convertExcelToPdf(file.getInputStream());

            // Trả về file PDF để tải xuống
            File pdfFile = new File(pdfFilePath);
            FileSystemResource resource = new FileSystemResource(pdfFile);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfFile.length())
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
