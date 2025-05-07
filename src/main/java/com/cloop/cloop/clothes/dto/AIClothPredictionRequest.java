package com.cloop.cloop.clothes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AIClothPredictionRequest {

    @Schema(description = "이미지 URL", example = "https://upload.wikimedia.org/wikipedia/commons/1/12/Tshirt.jpg")
    private String imageUrl;
}
