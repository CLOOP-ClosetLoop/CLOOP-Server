package com.cloop.cloop.clothes.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ClothResponse {
    private Long clothId;
    private String clothName;
    private String category;
    private String brand;
    private LocalDate purchasedAt;
    private String color;
    private String season;
    private Boolean donated;
    private String imageUrl;
    private LocalDate lastWornAt;
    private int wearCount;
}
