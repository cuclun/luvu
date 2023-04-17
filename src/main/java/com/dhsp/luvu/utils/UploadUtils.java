package com.dhsp.luvu.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.Date;
import java.time.LocalDate;

public class UploadUtils {
    public static String save(MultipartFile multipartFile) {
        String name = Date.valueOf(LocalDate.now()).toLocalDate() + "-" + multipartFile.getOriginalFilename();
        try {
            File file = new File("src/main/resources/uploads/" + name);
            try (OutputStream os = Files.newOutputStream(file.toPath())) {
                os.write(multipartFile.getBytes());
            }

            return name;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm ảnh!");
        }
    }
}
