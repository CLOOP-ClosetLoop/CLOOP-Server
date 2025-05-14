package com.cloop.cloop.looks.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class LookRequestDto {

    private String imageUrl;
    private List<Long> clothIds;
    private LocalDate wornDate;

}
