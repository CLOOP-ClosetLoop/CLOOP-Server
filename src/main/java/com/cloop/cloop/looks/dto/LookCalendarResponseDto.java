package com.cloop.cloop.looks.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class LookCalendarResponseDto {

    private Long lookId;
    private LocalDate createdAt;
    private String imageUrl;

}
