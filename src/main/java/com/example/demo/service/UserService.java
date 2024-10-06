package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @SneakyThrows
    public byte[] exportReport() throws FileNotFoundException {
        List<User> userList = userRepository.findAll();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("reports/account.jrxml");
        if (inputStream == null) {
            throw new FileNotFoundException("Tệp không tìm thấy trong classpath: reports/account.jrxml");
        }

        // Biên dịch báo cáo
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", "Tên Bảng của Bạn");

        // Điền dữ liệu vào báo cáo
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        // Xuất báo cáo ra ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        return outputStream.toByteArray();
    }
}
