package com.cloop.cloop.clothes.domain;


import com.cloop.cloop.auth.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cloth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clothId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(length = 20, nullable = false)
    private String clothName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category; // ENUM: 상의, 하의, 아우터, 신발, 가방, 모자, 기타

    @Column(length = 20)
    private String brand;

    private LocalDate purchasedAt;

    @Column(length = 20)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Season season; // 여름, 겨울

    @CreationTimestamp
    private LocalDate createdAt;

    private Boolean donated = false;

    private LocalDate lastWornAt;

    @Column(nullable = false)
    private String imageUrl;

}
