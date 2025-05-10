package com.cloop.cloop.clothes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Getter
public class ClothCreateRequest {

    @Schema(description = "옷 이름", example = "WHITE_BLOSSOM_TOP", defaultValue = "WHITE_BLOSSOM_TOP")
    private String clothName;

    @Schema(description = "카테고리", example = "TOP", defaultValue = "TOP")
    private String category;

    @Schema(description = "브랜드", example = "Nike", defaultValue = "Nike")
    private String brand;

    @Schema(description = "구입 날짜", example = "2025-05-01", defaultValue = "2025-05-01")
    private LocalDate purchasedAt;

    @Schema(description = "색상", example = "WHITE", defaultValue = "WHITE")
    private String color;

    @Schema(description = "계절", example = "SUMMER", defaultValue = "SUMMER")
    private String season;

    @Schema(description = "이미지 URL", example = "http://localhost:8081/uploads/example.png")
    private String imageUrl;
}