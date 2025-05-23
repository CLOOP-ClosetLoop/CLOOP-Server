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
@Tag(name = "Cloth", description = "옷 등록 및 조회 관련 API")
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
    @Operation(summary = "전체 옷 정보 조회")
    @GetMapping
    public ResponseEntity<?> getAllClothes(@AuthenticationPrincipal Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        return ResponseEntity.ok(clothService.getAllClothes(user));
    }
    @Operation(summary = "옷 기부 후보 조회", description = "6개월 이상 입지않은 옷을 조회합니다.")
    @GetMapping("/donation-candidates")
    public ResponseEntity<?> getDonationCandidates(@AuthenticationPrincipal Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        return ResponseEntity.ok(clothService.getDonationCandidates(user));
    }
    @Operation(summary = "옷 기부 상태 변경", description = "옷을 기부상태로 변경합니다.")
    @PatchMapping("/{clothId}/donate")
    public ResponseEntity<?> donateCloth(@PathVariable Long clothId,
                                         @RequestBody Map<String, Boolean> request) {
        if (!Boolean.TRUE.equals(request.get("confirmed"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "기부 확인이 필요합니다."));
        }

        return ResponseEntity.ok(clothService.markAsDonated(clothId));
    }

    @Operation(summary = "옷 착용 통계", description = "전체 옷 착용 통계를 반환합니다")
    @GetMapping("/statistics")
    public ResponseEntity<?> getClothStatistics(@AuthenticationPrincipal Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        return ResponseEntity.ok(clothService.getClothStatistics(user));
    }

}