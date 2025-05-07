package com.cloop.cloop.clothes.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class AIClothPredictionResponse {
    private String predictedCategory;
    private String predictedColor;
    private double confidence;
}