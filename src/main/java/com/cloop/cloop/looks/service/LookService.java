package com.cloop.cloop.looks.service;

import com.cloop.cloop.auth.config.JwtUtil;
import com.cloop.cloop.auth.domain.User;
import com.cloop.cloop.auth.repository.UserRepository;
import com.cloop.cloop.clothes.domain.Cloth;
import com.cloop.cloop.clothes.repository.ClothRepository;
import com.cloop.cloop.global.file.ImageService;
import com.cloop.cloop.looks.domain.Look;
import com.cloop.cloop.looks.domain.LookCloth;
import com.cloop.cloop.looks.dto.LookCalendarResponseDto;
import com.cloop.cloop.looks.dto.LookRequestDto;
import com.cloop.cloop.looks.dto.LookResponseDto;
import com.cloop.cloop.looks.repository.LookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LookService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final LookRepository lookRepository;
    private final ClothRepository clothRepository;

    @Transactional
    public LookResponseDto createLook(LookRequestDto lookRequestDto, String token) {

        Long userId = jwtUtil.extractUserId(token); // JWT에서 userId 추출
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 userId를 찾을 수 없습니다."));

        if (lookRequestDto.getImageUrl() == null || lookRequestDto.getImageUrl().isEmpty()) {
            throw new IllegalArgumentException("이미지를 등록해 주세요.");
        }

        // 사용자가 등록한 모든 Cloth 조회
        List<Cloth> userClothes = clothRepository.findAllById(lookRequestDto.getClothIds());

        // Look에 등록할 cloth 필터링
        List<Long> selectedClothIds = lookRequestDto.getClothIds();
        List<Cloth> selectedCloths = userClothes.stream()
                .filter(cloth -> selectedClothIds.contains(cloth.getClothId())).toList();

        if (selectedCloths.isEmpty()) {
            throw new IllegalArgumentException("선택된 옷이 사용자 목록에 존재하지 않습니다.");
        }

        // LookCloth 생성 (Builder 패턴 사용)
        List<LookCloth> lookClothList = selectedCloths.stream()
                .map(cloth -> LookCloth.builder()
                        .cloth(cloth)
                        .build())
                .toList();

        // Look 생성 (Builder 패턴 사용)
        Look look = Look.builder()
                .user(user)
                .createdAt(LocalDate.now())
                .lookClothList(lookClothList)
                .build();

        // Look과 LookCloth 연결
        lookClothList.forEach(lookCloth -> lookCloth.setLook(look));

        // Look 저장
        Look savedLook = lookRepository.save(look);

        return LookResponseDto.builder()
                .lookId(savedLook.getLookId())
                .createdAt(savedLook.getCreatedAt())
                .message("착장이 성공적으로 저장되었습니다.")
                .build();

    }

    // 날짜별 look 조회
    @Transactional(readOnly = true)
    public List<LookCalendarResponseDto> getLooksByDate(String date, String token){

        Long userId = jwtUtil.extractUserId(token); // JWT에서 userId 추출
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 userId를 찾을 수 없습니다."));

        LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<Look> looks = lookRepository.findAllByCreatedAt(targetDate);

        return looks.stream()
                .map(look -> LookCalendarResponseDto.builder()
                        .lookId(look.getLookId())
                        .createdAt(look.getCreatedAt())
                        .imageUrl(getFirstImageUrl(look))
                        .build())
                .collect(Collectors.toList());
    }

    // Look의 첫 번째 이미지 URL 반환 (없을 경우 null)
    private String getFirstImageUrl(Look look) {
        return look.getLookImageList().isEmpty() ? null : look.getLookImageList().get(0).getBase64Image();
    }

    public void uploadLookImage(List<MultipartFile> imageList, Look look){
        imageService.saveImage(imageList, look);
    }

}
