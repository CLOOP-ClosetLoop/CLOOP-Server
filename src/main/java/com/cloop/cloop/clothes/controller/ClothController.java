package com.cloop.cloop.clothes.controller;

import com.cloop.cloop.auth.domain.User;
import com.cloop.cloop.auth.repository.UserRepository;
import com.cloop.cloop.clothes.dto.AIClothPredictionRequest;
import com.cloop.cloop.clothes.dto.ClothCreateRequest;
import com.cloop.cloop.clothes.dto.ClothCreateResponse;
import com.cloop.cloop.clothes.service.ClothService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/clothes")
@RequiredArgsConstructor
@Tag(name = "Cloth", description = "옷 AI 분류 관련 API")
public class ClothController {
    private final ClothService clothService;
    private final UserRepository userRepository;


    @Operation(summary = "옷 이미지 업로드", description = "이미지를 업로드하고 접근 가능한 URL을 반환합니다.")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile file) {
        return ResponseEntity.ok(clothService.uploadImage(file));
    }

    @Operation(summary = "AI로 옷 카테고리 분류", description = "이미지 URL을 기반으로 옷의 카테고리와 색상을 예측합니다.")
    @PostMapping("/ai")
    public ResponseEntity<?> classifyCloth(@RequestBody AIClothPredictionRequest request) {
        String imageUrl = request.getImageUrl();
        if (imageUrl == null || imageUrl.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "imageUrl이 필요합니다."));
        }
        return ResponseEntity.ok(clothService.classifyClothingByUrl(imageUrl));
    }
    @Operation(summary = "옷 업로드", description = "옷장에 등록할 옷을 업로드 합니다.")
    @PostMapping
    public ResponseEntity<?> createCloth(@RequestBody ClothCreateRequest request, @AuthenticationPrincipal Long userId) {
        try {
            // userId를 통해 실제 User 객체 조회
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

            ClothCreateResponse response = clothService.createCloth(request, user);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}