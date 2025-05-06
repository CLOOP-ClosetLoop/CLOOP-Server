package com.cloop.cloop.looks.domain;

import com.cloop.cloop.auth.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="look")
@Getter
@Setter
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
    private Date createdAt;

    // User와 FK 관계 설정
    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    private User user;

}
