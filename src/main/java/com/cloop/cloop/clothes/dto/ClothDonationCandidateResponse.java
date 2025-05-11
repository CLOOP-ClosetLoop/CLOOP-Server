package com.cloop.cloop.clothes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class ClothDonationCandidateResponse {
    private Long clothId;
    private String clothName;
    private LocalDate lastWornAt;
    private String imageUrl;
}
