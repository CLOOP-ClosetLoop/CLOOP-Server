package com.cloop.cloop.auth.domain;

import com.cloop.cloop.looks.domain.Look;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "`user`")
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

    // 착장 테이블과 1:N 관계
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Look> looks;
}