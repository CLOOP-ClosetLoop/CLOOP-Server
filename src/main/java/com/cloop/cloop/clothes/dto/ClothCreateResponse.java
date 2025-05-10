package com.cloop.cloop.clothes.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class ClothCreateResponse {
    private Long clothId;
    private String message;
}