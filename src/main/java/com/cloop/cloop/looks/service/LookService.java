package com.cloop.cloop.looks.service;

import com.cloop.cloop.global.file.ImageService;
import com.cloop.cloop.looks.domain.Look;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LookService {

    private final ImageService imageService;

    public void uploadLookImage(List<MultipartFile> imageList, Look look){
        imageService.saveImage(imageList, look);
    }

}
