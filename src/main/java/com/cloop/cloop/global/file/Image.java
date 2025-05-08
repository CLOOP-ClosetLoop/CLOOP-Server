package com.cloop.cloop.global.file;

import com.cloop.cloop.looks.domain.Look;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;

@Entity
@Table(name= "image")
@Inheritance(strategy = InheritanceType.JOINED)     // cloth_image, look_image 자식 테이블 지정
@Getter
@Setter
@NoArgsConstructor
public abstract class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    //  저장한 이미지 이름
    private String imageName;

    // 이미지 정보
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] imageData;

    // base64 문자열 추가
    public String getBase64Image() {
        return Base64.getEncoder().encodeToString(this.imageData);
    }

    public Image(String imageName, byte[] imageData) {
        this.imageName = imageName;
        this.imageData = imageData;
    }
}
