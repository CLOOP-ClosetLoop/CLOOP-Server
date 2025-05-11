package com.cloop.cloop.looks.domain;

import com.cloop.cloop.auth.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="look")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Look {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lookId;

//    @Column(nullable = false)
//    private String photo;

    // 착장 당 여러 개의 사진을 등록할 수 있음 -> 1 : N
    @OneToMany(mappedBy = "look", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LookImage> lookImageList = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate createdAt;

    // User와 FK 관계 설정
    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    private User user;

    // LookCloth 중간 테이블 설정
    @OneToMany(mappedBy = "look", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LookCloth> lookClothList = new ArrayList<>();

    // LookImage 추가 (연관관계 메서드)
    public void addLookImage(LookImage lookImage) {
        lookImage.setLook(this);
        this.lookImageList.add(lookImage);
    }

}
