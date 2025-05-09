package com.cloop.cloop.clothes.service;

import com.cloop.cloop.auth.domain.User;
import com.cloop.cloop.clothes.domain.Category;
import com.cloop.cloop.clothes.domain.Cloth;
import com.cloop.cloop.clothes.domain.Season;
import com.cloop.cloop.clothes.dto.AIClothPredictionResponse;
import com.cloop.cloop.clothes.repository.ClothRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.cloop.cloop.clothes.dto.ClothCreateRequest;
import com.cloop.cloop.clothes.dto.ClothCreateResponse;
@Service
@RequiredArgsConstructor
public class ClothService {
    private final ClothRepository clothRepository;
    @Value("${gemini.api.key}")
    private String apiKey;

    /**
     * 이미지 파일 업로드 후 URL 반환
     */
    public Map<String, String> uploadImage(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("uploads", filename);
            Files.createDirectories(uploadPath.getParent());
            Files.write(uploadPath, file.getBytes());

            String imageUrl = "http://localhost:8081/uploads/" + filename;
            return Map.of("imageUrl", imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패", e);
        }
    }

    public AIClothPredictionResponse classifyClothingByUrl(String imageUrl) {
        try {
            String filename = extractFilename(imageUrl);
            Path imagePath = Paths.get("uploads", filename);
            byte[] imageBytes = Files.readAllBytes(imagePath);
            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

            String prompt = """
            이미지에 있는 옷에 대해 다음 정보를 영어로 JSON 형식으로 생성해줘.
            
            1. 옷의 전체적인 색상을 예측해줘. (예: WHITE, BLACK, BLUE 등)
            
            2. 옷의 카테고리를 다음 중 하나로 예측해줘:
               - TOP, BOTTOM, OUTER, SHOES, BAG, HAT, ETC
               (반드시 이 목록 중 하나만 선택해서 반환할 것)
            
            3. 옷의 계절을 예측해줘: SUMMER 또는 WINTER 중 하나로만
            
            4. 위 정보를 바탕으로 스타일이 느껴지는 감성적인 영어 이름을 하나 생성해줘. 
               이름은 다음과 같은 형식으로 만들어:
               "{COLOR}_{STYLE_TAG}_{CATEGORY}"
               - STYLE_TAG는 감성적이고 분위기를 표현하는 영어 단어 중 하나로 해줘 (예: 부드럽거나 사랑스럽거나 빈티지한 느낌)
               - 무조건 영어 대문자로 작성해줘
            결과는 아래 JSON 형식으로 영어로 정확히 반환해줘:
            
            {
              "predictedName": "WHITE_BLOSSOM_TOP",
              "predictedColor": "WHITE",
              "predictedCategory": "TOP",
              "predictedSeason": "SUMMER",
              "confidence": 0.91
            }
            
            ⚠️ 다른 문장이나 설명은 절대 하지 말고 JSON만 정확하게 반환해.
            """;
            ObjectMapper mapper = new ObjectMapper();
            String escapedPrompt = mapper.writeValueAsString(prompt);

            String requestBody = "{"
                    + "\"contents\": [{"
                    + "  \"parts\": ["
                    + "    {\"text\": " + escapedPrompt + "},"
                    + "    {\"inlineData\": {\"mimeType\": \"image/jpeg\", \"data\": \"" + imageBase64 + "\"}}"
                    + "  ]"
                    + "}]"
                    + "}";

            String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());
            JsonNode candidates = root.path("candidates");
            if (!candidates.isArray() || candidates.isEmpty()) {
                throw new RuntimeException("AI 응답에 candidates 없음: " + response.body());
            }

            JsonNode parts = candidates.get(0).path("content").path("parts");
            if (!parts.isArray() || parts.isEmpty()) {
                throw new RuntimeException("AI 응답의 parts 없음: " + response.body());
            }

            String rawText = parts.get(0).path("text").asText();
            String resultJson = rawText
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            JsonNode resultNode = mapper.readTree(resultJson);

            return AIClothPredictionResponse.builder()
                    .predictedName(resultNode.path("predictedName").asText())
                    .predictedCategory(resultNode.path("predictedCategory").asText())
                    .predictedColor(resultNode.path("predictedColor").asText())
                    .predictedSeason(resultNode.path("predictedSeason").asText())
                    .confidence(resultNode.path("confidence").asDouble())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("AI 분석 실패: " + e.getMessage(), e);
        }
    }

    private String extractFilename(String imageUrl) {
        return Paths.get(URI.create(imageUrl).getPath()).getFileName().toString();
    }

    //옷등록하기
    public ClothCreateResponse createCloth(ClothCreateRequest req, User user) {

        if (req.getClothName() == null || req.getCategory() == null || req.getColor() == null || req.getSeason() == null) {
            throw new IllegalArgumentException("필수 정보를 모두 입력해 주세요.");
        }

        Cloth cloth = Cloth.builder()
                .user(user)
                .clothName(req.getClothName())
                .category(Category.valueOf(req.getCategory().toUpperCase()))
                .brand(req.getBrand())
                .color(req.getColor())
                .season(Season.valueOf(req.getSeason().toUpperCase()))
                .purchasedAt(req.getPurchasedAt())
                .imageUrl(req.getImageUrl())
                .donated(false)
                .build();

        Cloth saved = clothRepository.save(cloth);

        return ClothCreateResponse.builder()
                .clothId(saved.getClothId())
                .message("Cloth created successfully")
                .build();
    }
}
