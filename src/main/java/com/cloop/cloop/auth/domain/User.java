package com.cloop.cloop.auth.domain;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String googleId;

    @Column(nullable = false)
    private String nickname;

    @Column(length = 20, nullable = false)
    private String gender;
}
