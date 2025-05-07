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

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public AIClothPredictionResponse classifyClothing(MultipartFile file) throws IOException, InterruptedException {
        String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        String prompt = """
                이 옷의 카테고리와 색상을 예측해서 JSON 형식으로 출력해줘.
                카테고리는 TOP, BOTTOM, OUTER, SHOES 중 하나,
                색상은 WHITE, BLACK, RED 등 일반적인 색상으로.
                반환 형식은 다음과 같아:

                { "predictedCategory": "TOP", "predictedColor": "WHITE", "confidence": 0.91 }
                
                JSON 이외의 다른 말은 하지마.
                """;

        ObjectMapper mapper = new ObjectMapper();
        String escapedPrompt = mapper.writeValueAsString(prompt); // 이스케이프된 문자열 생성
        String imageBase64 = encodeImage(file);

        String requestBody = "{"
                + "\"contents\": [{"
                + "  \"parts\": ["
                + "    {\"text\": " + escapedPrompt + "},"
                + "    {\"inlineData\": {\"mimeType\": \"" + file.getContentType() + "\", \"data\": \"" + imageBase64 + "\"}}"
                + "  ]"
                + "}]"
                + "}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 응답 출력 로그
        System.out.println("Gemini 응답: " + response.body());

        JsonNode root = mapper.readTree(response.body());

        JsonNode candidates = root.path("candidates");
        if (!candidates.isArray() || candidates.size() == 0) {
            throw new RuntimeException("AI 응답에 candidates 없음: " + response.body());
        }

        JsonNode parts = candidates.get(0).path("content").path("parts");
        if (!parts.isArray() || parts.size() == 0) {
            throw new RuntimeException("AI 응답의 parts 없음: " + response.body());
        }

        // 🔧 마크다운 백틱 제거 + 공백 제거
        String rawText = parts.get(0).path("text").asText();
        String resultJson = rawText
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

        // text 안에 있는 JSON 문자열 다시 파싱
        JsonNode resultNode = mapper.readTree(resultJson);
        String category = resultNode.path("predictedCategory").asText();
        String color = resultNode.path("predictedColor").asText();
        double confidence = resultNode.path("confidence").asDouble();

        return AIClothPredictionResponse.builder()
                .predictedCategory(category)
                .predictedColor(color)
                .confidence(confidence)
                .build();
    }

    private String encodeImage(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }
}
