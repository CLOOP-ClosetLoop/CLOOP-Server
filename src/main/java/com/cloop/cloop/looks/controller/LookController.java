package com.cloop.cloop.looks.controller;

import com.cloop.cloop.auth.config.JwtUtil;
import com.cloop.cloop.auth.domain.User;
import com.cloop.cloop.auth.repository.UserRepository;
import com.cloop.cloop.global.file.FileHandler;
import com.cloop.cloop.looks.domain.Look;
import com.cloop.cloop.looks.dto.LookCalendarResponseDto;
import com.cloop.cloop.looks.dto.LookRequestDto;
import com.cloop.cloop.looks.dto.LookResponseDto;
import com.cloop.cloop.looks.service.LookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/looks")
@RequiredArgsConstructor
public class LookController {

    private final FileHandler fileHandler;
    private final LookService lookService;

    // 착장 사진 등록
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

    // 착장 등록하기
    @PostMapping
    public ResponseEntity<?> createLook(
            @RequestHeader("Authorization") String token,@RequestBody LookRequestDto lookRequestDto) {

        // Authorization 헤더에서 "Bearer " 제거
        String accessToken = token.replace("Bearer ", ""); // "Bearer " 제거

        if (lookRequestDto.getClothIds() == null || lookRequestDto.getClothIds().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "입은 옷을 선택해 주세요."));
        }

        try {
            LookResponseDto response = lookService.createLook(lookRequestDto, accessToken);
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e ){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "서버 오류 : " + e.getMessage()));
        }

    }

    // 날짜별 착용 조회
    @GetMapping
    public ResponseEntity<?> getLooksByDate(
            @RequestHeader("Authorization") String token,
            @RequestParam("date") String date) {

        // Authorization 헤더에서 "Bearer " 제거
        String accessToken = token.replace("Bearer ", ""); // "Bearer " 제거

        try{
            List<LookCalendarResponseDto> looks = lookService.getLooksByDate(date, accessToken);

            if (looks == null || looks.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "해당 날짜에 등록된 착장이 없습니다."));
            }
            return ResponseEntity.status(200).body(looks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "서버 오류 : " + e.getMessage()));
        }

    }
    //사용자가 착장 등록한 날짜들 리스트로 가져오기
    @GetMapping("/dates")
    public ResponseEntity<?> getLookDates(@RequestHeader("Authorization") String token) {
        String accessToken = token.replace("Bearer ", "");
        return ResponseEntity.ok(lookService.getLookDates(accessToken));
    }

}
