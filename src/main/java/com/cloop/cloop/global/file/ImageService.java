package com.cloop.cloop.global.file;

import com.cloop.cloop.looks.domain.Look;
import com.cloop.cloop.looks.domain.LookImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void saveImage(List<MultipartFile> imageList, Look look) {

        // 업로드한 파일이 없을 경우
        if (imageList == null) {
            return;
        }

        // 업로드한 사진의 타입 검사
        for (MultipartFile image : imageList) {
            if (!isImageFile(image)) throw new IllegalArgumentException(".jpg, .png, .svg 등 이미지 파일만 업로드 가능합니다.");
        }

        try {
            // 각 사진들을 db에 저장
            for (MultipartFile image : imageList) {

                // 파일이 비어 있을 경우
                if(Objects.requireNonNull(image.getOriginalFilename()).isEmpty()) continue;

                // 이미지 이름 중복 방지 -> UUID 추가
                String imageName = addUUID(image.getOriginalFilename());

                // lookImage 엔티티 생성, Look과 연관 관계 설정
                LookImage lookImages = new LookImage(imageName, image.getBytes());
                lookImages.setLook(look);

                // 생성한 lookImage 엔티티 저장
                imageRepository.save(lookImages);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일이 이미지 형식인지 검사하는 메서드
    private static boolean isImageFile(MultipartFile image) {
        String contentType = image.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    // 이미지 이름 앞에 UUID 추가 (이미지 이름 중복을 피하기 위함)
    private static String addUUID(String originalFilename) {
        return UUID.randomUUID().toString().replace("-", "") + "_" + originalFilename;
    }

}
