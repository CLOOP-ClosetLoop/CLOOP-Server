package com.cloop.cloop.looks.dto;

import com.cloop.cloop.clothes.dto.ClothResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class LookCalendarResponseDto {

    private Long lookId;
    private LocalDate createdAt;
    private String imageUrl;
    private List<ClothResponse> clothes;

}
