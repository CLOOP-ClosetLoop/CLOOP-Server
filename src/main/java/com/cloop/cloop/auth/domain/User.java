package com.cloop.cloop.auth.domain;

import com.cloop.cloop.looks.domain.Look;
import jakarta.persistence.*;

import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "User", uniqueConstraints = {
        @UniqueConstraint(columnNames = "googleId"),
        @UniqueConstraint(columnNames = "nickname")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String googleId;

    @Column(unique = true, nullable = false, length = 8)
    private String nickname;

    @Column(length = 20, nullable = false)
    private String gender;

    // 착장 테이블과 1:N 관계
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Look> looks;

}
