package com.cloop.cloop.looks.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LookRequestDto {

    private String imageUrl;
    private List<Long> clothIds;

}
