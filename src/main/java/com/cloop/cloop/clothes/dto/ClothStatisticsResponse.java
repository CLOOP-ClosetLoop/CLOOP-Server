package com.cloop.cloop.clothes.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClothStatisticsResponse {
    private Long clothId;
    private String clothName;
    private long wearCount;
    private LocalDate lastWornAt;
    private String imageUrl;
}