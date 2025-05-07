package com.cloop.cloop.clothes.service;

import com.cloop.cloop.clothes.dto.AIClothPredictionResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class GeminiService {

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
                이 옷의 카테고리와 색상을 예측해서 JSON 형식으로 출력해줘.
                카테고리는 TOP, BOTTOM, OUTER, SHOES 중 하나,
                색상은 WHITE, BLACK, RED 등 일반적인 색상으로.
                반환 형식은 다음과 같아:

                { "predictedCategory": "TOP", "predictedColor": "WHITE", "confidence": 0.91 }

                JSON 이외의 다른 말은 하지마.
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
                    .predictedCategory(resultNode.path("predictedCategory").asText())
                    .predictedColor(resultNode.path("predictedColor").asText())
                    .confidence(resultNode.path("confidence").asDouble())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("AI 분석 실패: " + e.getMessage(), e);
        }
    }

    private String extractFilename(String imageUrl) {
        return Paths.get(URI.create(imageUrl).getPath()).getFileName().toString();
    }
//    public AIClothPredictionResponse classifyClothing(MultipartFile file) throws IOException, InterruptedException {
//        String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;
//
//        String prompt = """
//                이 옷의 카테고리와 색상을 예측해서 JSON 형식으로 출력해줘.
//                카테고리는 TOP, BOTTOM, OUTER, SHOES 중 하나,
//                색상은 WHITE, BLACK, RED 등 일반적인 색상으로.
//                반환 형식은 다음과 같아:
//
//                { "predictedCategory": "TOP", "predictedColor": "WHITE", "confidence": 0.91 }
//
//                JSON 이외의 다른 말은 하지마.
//                """;
//
//        ObjectMapper mapper = new ObjectMapper();
//        String escapedPrompt = mapper.writeValueAsString(prompt); // 이스케이프된 문자열 생성
//        String imageBase64 = encodeImage(file);
//
//        String requestBody = "{"
//                + "\"contents\": [{"
//                + "  \"parts\": ["
//                + "    {\"text\": " + escapedPrompt + "},"
//                + "    {\"inlineData\": {\"mimeType\": \"" + file.getContentType() + "\", \"data\": \"" + imageBase64 + "\"}}"
//                + "  ]"
//                + "}]"
//                + "}";
//
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(endpoint))
//                .header("Content-Type", "application/json")
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        // 응답 출력 로그
//        System.out.println("Gemini 응답: " + response.body());
//
//        JsonNode root = mapper.readTree(response.body());
//
//        JsonNode candidates = root.path("candidates");
//        if (!candidates.isArray() || candidates.size() == 0) {
//            throw new RuntimeException("AI 응답에 candidates 없음: " + response.body());
//        }
//
//        JsonNode parts = candidates.get(0).path("content").path("parts");
//        if (!parts.isArray() || parts.size() == 0) {
//            throw new RuntimeException("AI 응답의 parts 없음: " + response.body());
//        }
//
//        // 🔧 마크다운 백틱 제거 + 공백 제거
//        String rawText = parts.get(0).path("text").asText();
//        String resultJson = rawText
//                .replaceAll("```json", "")
//                .replaceAll("```", "")
//                .trim();
//
//        // text 안에 있는 JSON 문자열 다시 파싱
//        JsonNode resultNode = mapper.readTree(resultJson);
//        String category = resultNode.path("predictedCategory").asText();
//        String color = resultNode.path("predictedColor").asText();
//        double confidence = resultNode.path("confidence").asDouble();
//
//        return AIClothPredictionResponse.builder()
//                .predictedCategory(category)
//                .predictedColor(color)
//                .confidence(confidence)
//                .build();
//    }
//
//    private String encodeImage(MultipartFile file) throws IOException {
//        byte[] bytes = file.getBytes();
//        return Base64.getEncoder().encodeToString(bytes);
//    }

}
