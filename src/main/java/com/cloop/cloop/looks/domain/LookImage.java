package com.cloop.cloop.looks.domain;

import com.cloop.cloop.global.file.Image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "lookImage")       // image 엔티티를 상속 받아 자식 테이블로 생성
@Getter
@Setter
@NoArgsConstructor
public class LookImage extends Image {

    // N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lookId")
    private Look look;

    // 연관 관계 편의 메서드
    public void setLook (Look look) {
        this.look = look;
        if (look != null && !look.getLookImageList().contains(this)) {
            look.getLookImageList().add(this);
        }
    }

    public LookImage(String imageName, byte[] imageData) {
        super(imageName, imageData);
    }

}
