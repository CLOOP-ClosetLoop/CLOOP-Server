package com.cloop.cloop.looks.controller;

import com.cloop.cloop.global.file.FileHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/looks")
@RequiredArgsConstructor
public class LookController {

    private final FileHandler fileHandler;

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "사진을 등록해 주세요."));
        }

        try {
            String imageUrl = fileHandler.saveFile(image);
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "이미지 업로드 실패: " + e.getMessage()));
        }
    }

}
