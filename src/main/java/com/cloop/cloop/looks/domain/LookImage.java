package com.cloop.cloop.looks.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;

@Entity
@Table(name= "lookImage")
@Getter
@Setter
@NoArgsConstructor
public class LookImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    // N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lookId")
    private Look look;

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

    // 연관 관계 편의 메서드
    public void setLook (Look look) {
        this.look = look;
        look.getLookImageList().add(this);
    }

    public LookImage(String imageName, byte[] imageData) {
        this.imageName = imageName;
        this.imageData = imageData;
    }

}
