package com.cloop.cloop.global.file;

import com.cloop.cloop.looks.domain.Look;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;

@Entity
@Table(name= "image")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "image_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public abstract class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    //  저장한 이미지 이름
    private String imageName;

    // 이미지 URL (URL로 저장된 경우)
    @Column(nullable = true)
    private String imageUrl;

    // 이미지 정보
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] imageData;

    // base64 문자열로 이미지 변환
    public String getBase64Image() {
        return (imageData != null) ? Base64.getEncoder().encodeToString(this.imageData) : null;
    }

    // 이미지 URL 또는 Base64 이미지 반환
    public String getDisplayImage() {
        return (imageUrl != null) ? imageUrl : getBase64Image();
    }

    public Image(String imageName, byte[] imageData) {
        this.imageName = imageName;
        this.imageData = imageData;
    }

    public Image(String imageName, String imageUrl) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }
}
