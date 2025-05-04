package com.cloop.cloop.auth.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")       //user: 예약어 -> users라고 테이블명 셋팅
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String googleId;

    @Column(unique = true, nullable = false, length = 8)
    private String nickname;

    @Column(nullable = false)
    private String gender;
}
