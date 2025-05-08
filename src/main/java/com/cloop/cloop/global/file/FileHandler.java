package com.cloop.cloop.global.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileHandler {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/look/";

    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일입니다.");
        }

        String originalFilename = file.getOriginalFilename();
        String photoType = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID() + photoType;

        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File dest = new File(UPLOAD_DIR + newFilename);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
        }

        return UPLOAD_DIR + newFilename;
    }
}
