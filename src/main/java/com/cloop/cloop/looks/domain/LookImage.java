package com.cloop.cloop.looks.domain;

import com.cloop.cloop.global.file.Image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("LOOK_IMAGE")
@Getter
@Setter
@NoArgsConstructor
public class LookImage extends Image {

    // N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lookId", nullable = false)
    private Look look;

    // 연관 관계 편의 메서드
    public void setLook (Look look) {
        this.look = look;
        if (look != null && !look.getLookImageList().contains(this)) {
            look.getLookImageList().add(this);
        }
    }

    // 이미지 URL 생성자
    public LookImage(String imageName, String imageUrl) {
        super(imageName, imageUrl);
    }

    // Base64 이미지 생성자
    public LookImage(String imageName, byte[] imageData) {
        super(imageName, imageData);
    }

    // 이미지 URL 또는 Base64 이미지 반환
    public String getDisplayImage() {
        return (getImageUrl() != null) ? getImageUrl() : getBase64Image();
    }

}
