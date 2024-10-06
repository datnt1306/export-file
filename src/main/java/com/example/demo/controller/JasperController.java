package com.example.demo.controller;

import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class JasperController {

    private final UserService userService;

    // API in báo cáo - Trả về file PDF dưới dạng byte[]
    @GetMapping("/print")
    public ResponseEntity<Resource> printReport() {
        try {
            byte[] reportBytes = userService.exportReport();

            ByteArrayResource resource = new ByteArrayResource(reportBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportReportToFile(@RequestParam String filePath) {
        try {
            byte[] reportBytes = userService.exportReport();

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(reportBytes);
            }

            return new ResponseEntity<>("Báo cáo đã được lưu tại: " + filePath, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Lỗi khi lưu báo cáo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
