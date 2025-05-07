package com.cloop.cloop.clothes.controller;

import com.cloop.cloop.clothes.dto.AIClothPredictionResponse;
import com.cloop.cloop.clothes.service.GeminiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/clothes")
@RequiredArgsConstructor
@Tag(name = "Cloth", description = "옷 AI 분류 관련 API")
public class ClothAIController {

    private final GeminiService geminiService;

    @Operation(summary = "AI로 옷 카테고리 분류", description = "이미지 파일을 기반으로 옷의 카테고리와 색상을 예측합니다.")
    @PostMapping(value = "/ai", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> classifyCloth(@RequestParam("image") MultipartFile file) {
        try {
            AIClothPredictionResponse result = geminiService.classifyClothing(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("분석 실패: " + e.getMessage());
        }
    }
}